package com.project.swimcb.config.security;

import static com.project.swimcb.token.domain.enums.MemberRole.ADMIN;
import static com.project.swimcb.token.domain.enums.MemberRole.CUSTOMER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

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
import org.junit.jupiter.api.Nested;
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
  
  @Mock
  private FilterChain chain;

  @BeforeEach
  void setUp() {
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
  }

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Nested
  @DisplayName("유효한 JWT 토큰으로 요청시")
  class ValidJwtToken {

    @Test
    @DisplayName("ADMIN 역할의 토큰인 경우 SecurityContext에 인증 정보를 설정한다")
    void setAuthenticationForAdminToken() throws ServletException, IOException {
      // given
      val memberId = 1L;
      val role = ADMIN;
      val swimmingPoolId = 1L;
      val token = "valid_admin_token";

      request.addHeader("Authorization", TOKEN_PREFIX + token);

      val decodedJWT = mock(DecodedJWT.class);
      given(decodedJWT.getSubject()).willReturn(String.valueOf(memberId));

      val roleClaim = mock(Claim.class);
      given(roleClaim.asString()).willReturn(role.name());
      given(decodedJWT.getClaim("role")).willReturn(roleClaim);

      val swimmingPoolIdClaim = mock(Claim.class);
      given(swimmingPoolIdClaim.asLong()).willReturn(swimmingPoolId);
      given(decodedJWT.getClaim("swimmingPoolId")).willReturn(swimmingPoolIdClaim);

      given(jwtPort.parseToken(token)).willReturn(decodedJWT);

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

      then(jwtPort).should().parseToken(token);
      then(chain).should().doFilter(request, response);
    }

    @Test
    @DisplayName("CUSTOMER 역할의 토큰인 경우 SecurityContext에 인증 정보를 설정한다")
    void setAuthenticationForCustomerToken() throws ServletException, IOException {
      // given
      val memberId = 2L;
      val role = CUSTOMER;
      val token = "valid_customer_token";

      request.addHeader("Authorization", TOKEN_PREFIX + token);

      val decodedJWT = mock(DecodedJWT.class);
      given(decodedJWT.getSubject()).willReturn(String.valueOf(memberId));

      val roleClaim = mock(Claim.class);
      given(roleClaim.asString()).willReturn(role.name());
      given(decodedJWT.getClaim("role")).willReturn(roleClaim);

      given(decodedJWT.getClaim("swimmingPoolId")).willReturn(null);

      given(jwtPort.parseToken(token)).willReturn(decodedJWT);

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
      assertThat(tokenInfo.swimmingPoolId()).isNull();

      then(jwtPort).should().parseToken(token);
      then(chain).should().doFilter(request, response);
    }
  }

  @Nested
  @DisplayName("Authorization 헤더가 없거나 유효하지 않은 경우")
  class InvalidAuthorizationHeader {

    @Test
    @DisplayName("Authorization 헤더가 null인 경우 필터를 통과시키고 인증 정보를 설정하지 않는다")
    void proceedWithoutAuthenticationWhenHeaderIsNull()
        throws ServletException, IOException {
      // given
      // Authorization 헤더 설정 안함

      // when
      filter.doFilterInternal(request, response, chain);

      // then
      then(jwtPort).should(never()).parseToken(anyString());
      assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
      then(chain).should().doFilter(request, response);
    }

    @Test
    @DisplayName("Authorization 헤더가 Bearer로 시작하지 않는 경우 필터를 통과시키고 인증 정보를 설정하지 않는다")
    void proceedWithoutAuthenticationWhenHeaderDoesNotStartWithBearer()
        throws ServletException, IOException {
      // given
      val invalidHeader = "Basic some_credential";
      request.addHeader("Authorization", invalidHeader);

      // when
      filter.doFilterInternal(request, response, chain);

      // then
      then(jwtPort).should(never()).parseToken(anyString());
      assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
      then(chain).should().doFilter(request, response);
    }
  }

  @Nested
  @DisplayName("JWT 토큰 검증 실패시")
  class JwtTokenValidationFailure {

    @Test
    @DisplayName("잘못된 서명의 토큰인 경우 request attribute에 에러 메시지를 설정하고 필터 체인을 계속 진행한다")
    void setErrorMessageAndProceedWhenSignatureVerificationFails()
        throws ServletException, IOException {
      // given
      val token = "invalid_signature_token";
      request.addHeader("Authorization", TOKEN_PREFIX + token);

      given(jwtPort.parseToken(token)).willThrow(
          new SignatureVerificationException(Algorithm.HMAC256("secret")));

      // when
      filter.doFilterInternal(request, response, chain);

      // then
      assertThat(request.getAttribute("errorMessage")).isEqualTo("잘못된 JWT 서명입니다.");
      assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

      then(jwtPort).should().parseToken(token);
      then(chain).should().doFilter(request, response);
    }

    @Test
    @DisplayName("만료된 토큰인 경우 request attribute에 에러 메시지를 설정하고 필터 체인을 계속 진행한다")
    void setErrorMessageAndProceedWhenTokenIsExpired()
        throws ServletException, IOException {
      // given
      val token = "expired_token";
      request.addHeader("Authorization", TOKEN_PREFIX + token);

      given(jwtPort.parseToken(token)).willThrow(
          new TokenExpiredException("Token expired", Instant.MIN));

      // when
      filter.doFilterInternal(request, response, chain);

      // then
      assertThat(request.getAttribute("errorMessage")).isEqualTo("만료된 JWT 서명입니다.");
      assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

      then(jwtPort).should().parseToken(token);
      then(chain).should().doFilter(request, response);
    }
  }
}
