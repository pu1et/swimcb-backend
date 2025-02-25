package com.project.swimcb.token.application;

import static com.project.swimcb.token.domain.enums.MemberRole.ADMIN;
import static com.project.swimcb.token.domain.enums.MemberRole.CUSTOMER;
import static com.project.swimcb.token.domain.enums.MemberRole.GUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.project.swimcb.token.application.out.JwtSecretGateway;
import com.project.swimcb.token.domain.TokenInfo;
import java.time.Instant;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtInteractorTest {

  @InjectMocks
  private JwtInteractor jwtInteractor;

  @Mock
  private JwtSecretGateway jwtSecretGateway;

  private static final String JWT_SECRET = "valid_secret";

  @BeforeEach
  void setUp() {
    when(jwtSecretGateway.getSecret()).thenReturn(JWT_SECRET);
  }

  @Test
  @DisplayName("게스트 토큰은 subject가 null이어야 한다.")
  void shouldHaveNullSubjectWhenGuestToken() {
    // given
    // when
    val guestToken = jwtInteractor.generateToken(TokenInfo.guest());
    // then
    assertThat(guestToken).isNotNull();

    val decoded = jwtInteractor.parseToken(guestToken);
    assertThat(decoded.getSubject()).isNull();
    assertThat(decoded.getClaim("role").asString()).isEqualTo(GUEST.name());
  }

  @Test
  @DisplayName("회원-고객 토큰은 subject는 memberId, role은 CUSTOMER이어야 한다.")
  void shouldHaveNullSubjectWhenCustomerToken() {
    // given
    val memberId = 1L;
    // when
    val customerToken = jwtInteractor.generateToken(TokenInfo.customer(memberId));
    // then
    assertThat(customerToken).isNotNull();

    val decoded = jwtInteractor.parseToken(customerToken);
    assertThat(decoded.getSubject()).isEqualTo(String.valueOf(memberId));
    assertThat(decoded.getClaim("role").asString()).isEqualTo(CUSTOMER.name());
  }

  @Test
  @DisplayName("회원-관리자 토큰은 subject는 memberId, role은 ADMIN이어야 한다.")
  void shouldHaveNullSubjectWhenAdminToken() {
    // given
    val memberId = 1L;
    // when
    val customerToken = jwtInteractor.generateToken(TokenInfo.admin(memberId));
    // then
    assertThat(customerToken).isNotNull();

    val decoded = jwtInteractor.parseToken(customerToken);
    assertThat(decoded.getSubject()).isEqualTo(String.valueOf(memberId));
    assertThat(decoded.getClaim("role").asString()).isEqualTo(ADMIN.name());
  }

  @Test
  @DisplayName("잘못된 secret으로 토큰 파싱시 SignatureVerificationException이 발생해야 한다.")
  void shouldThrowExceptionWhenSecretIsWrong() {
    // given
    when(jwtSecretGateway.getSecret()).thenReturn(JWT_SECRET, "wrong_secret");
    // when
    val token = jwtInteractor.generateToken(TokenInfo.guest());
    // then
    assertThrowsExactly(SignatureVerificationException.class,
        () -> jwtInteractor.parseToken(token));
  }

  @Test
  @DisplayName("만료된 토큰을 파싱시 TokenExpiredException이 발생해야 한다.")
  void shouldThrowExceptionWhenTokenIsExpired() {
    // given
    val expiredToken = JWT.create()
        .withIssuer("swimcb")
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now())
        .withSubject(null)
        .withClaim("role", GUEST.name())
        .sign(Algorithm.HMAC256(JWT_SECRET));
    // when
    // then
    assertThrowsExactly(TokenExpiredException.class,
        () -> jwtInteractor.parseToken(expiredToken));
  }
}