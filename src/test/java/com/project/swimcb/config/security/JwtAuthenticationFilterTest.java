package com.project.swimcb.config.security;

import static com.project.swimcb.token.domain.enums.MemberRole.ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.domain.TokenInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.time.Instant;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

  @InjectMocks
  private JwtAuthenticationFilter filter;

  @Mock
  private JwtPort jwtPort;

  private final String TOKEN_PREFIX = "Bearer ";

  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private FilterChain chain;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    chain = (req, res) -> {

    };
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  @DisplayName("유효한 토큰의 경우 SecurityContextHolder에 인증정보가 설정되어야 한다.")
  void shouldSetSecurityContextHolderWhenValidTokenProvided() throws ServletException, IOException {
    // given
    val memberId = 1L;
    val role = ADMIN;
    val swimmingPoolId = 1L;
    val token = "valid_token";

    request.addHeader("Authorization", TOKEN_PREFIX + token);

    val decodedJWT = mock(DecodedJWT.class);
    when(decodedJWT.getSubject()).thenReturn(String.valueOf(memberId));

    val roleClaim = mock(Claim.class);
    when(roleClaim.asString()).thenReturn(role.name());
    when(decodedJWT.getClaim("role")).thenReturn(roleClaim);

    val swimmingPoolIdClaim = mock(Claim.class);
    when(swimmingPoolIdClaim.asLong()).thenReturn(swimmingPoolId);
    when(decodedJWT.getClaim("swimmingPoolId")).thenReturn(swimmingPoolIdClaim);

    when(jwtPort.parseToken(token)).thenReturn(decodedJWT);

    // when
    filter.doFilterInternal(request, response, chain);

    // then
    val authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNotNull();
    assertThat(authentication.isAuthenticated()).isTrue();

    assertThat(authentication.getPrincipal()).isInstanceOf(TokenInfo.class);
    val tokenInfo = (TokenInfo) authentication.getPrincipal();
    assertThat(tokenInfo.memberId()).isEqualTo(memberId);
    assertThat(tokenInfo.role()).isEqualTo(role);
    assertThat(tokenInfo.swimmingPoolId()).isEqualTo(swimmingPoolId);
  }

  @Test
  @DisplayName("Authorization 헤더가 null인 경우 SecurityContextHolder에 인증정보를 설정하지 않는다.")
  void shouldNotCallJwtPortAndProceedWhenAuthorizationHeaderIsNull()
      throws ServletException, IOException {

    // given
    // when
    filter.doFilterInternal(request, response, chain);
    // then
    verify(jwtPort, never()).parseToken(anyString());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  @DisplayName("Authorization 헤더가 Bearer로 시작하지 않는 경우 SecurityContextHolder에 인증정보가 설정하지 않는다.")
  void shouldNotCallJwtPortAndProceedWhenAuthorizationHeaderDoesNotContainTokenPrefix()
      throws ServletException, IOException {

    // given
    val invalidHeader = "invalid_header";
    request.addHeader("Authorization", invalidHeader);
    // when
    filter.doFilterInternal(request, response, chain);
    // then
    verify(jwtPort, never()).parseToken(anyString());
    assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
  }

  @Test
  @DisplayName("잘못 서명된 토큰의 경우 SecurityContextHolder에 인증정보가 설정되지 않아야 한다.")
  void shouldNotSetAuthenticationWhenSignatureVerificationExceptionOccurs()
      throws ServletException, IOException {

    // given
    val token = "invalid_token";

    request.addHeader("Authorization", TOKEN_PREFIX + token);

    when(jwtPort.parseToken(token)).thenThrow(
        new SignatureVerificationException(Algorithm.HMAC256("secret")));

    // when
    filter.doFilterInternal(request, response, chain);
    // then
    val authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNull();
  }

  @Test
  @DisplayName("만료된 토큰의 경우 SecurityContextHolder에 인증정보가 설정되지 않아야 한다.")
  void shouldNotSetAuthenticationWhenTokenExpiredExceptionOccurs()
      throws ServletException, IOException {

    // given
    val token = "expired_token";

    request.addHeader("Authorization", TOKEN_PREFIX + token);

    when(jwtPort.parseToken(token)).thenThrow(
        new TokenExpiredException("Token expired", Instant.MIN));

    // when
    filter.doFilterInternal(request, response, chain);
    // then
    val authentication = SecurityContextHolder.getContext().getAuthentication();
    assertThat(authentication).isNull();
  }
}
