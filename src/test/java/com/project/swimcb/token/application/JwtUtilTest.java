package com.project.swimcb.token.application;

import static com.project.swimcb.token.domain.enums.MemberRole.ADMIN;
import static java.time.temporal.ChronoUnit.DAYS;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

  @InjectMocks
  private JwtInteractor jwtUtil;

  @Mock
  private JwtSecretGateway jwtSecretGateway;

  @Test
  @DisplayName("유효한 TokenInfo로 생성한 토큰은 올바른 클레임을 포함해야 한다.")
  void shouldContainCorrectClaimsWhenGeneratedWithValidTokenInfo() {
    // given
    val memberId = 1L;
    val isGuest = false;
    val role = ADMIN;

    val tokenInfo = new TokenInfo(memberId, isGuest, role);

    when(jwtSecretGateway.getSecret()).thenReturn("valid_secret");
    // when
    val generatedToken = jwtUtil.generateToken(tokenInfo);
    val decodedToken = jwtUtil.parseToken(generatedToken);
    // then
    assertThat(generatedToken).isNotNull();

    assertThat(decodedToken.getIssuer()).isEqualTo("swimcb");
    assertThat(decodedToken.getSubject()).isEqualTo("1");
    assertThat(decodedToken.getClaim("isGuest").asBoolean()).isEqualTo(isGuest);
    assertThat(decodedToken.getClaim("role").asString()).isEqualTo(role.name());

    val issuedAt = decodedToken.getIssuedAt().toInstant();
    val expiresAt = decodedToken.getExpiresAt().toInstant();
    assertThat(expiresAt).isEqualTo(issuedAt.plus(30, DAYS));
  }

  @Test
  @DisplayName("잘못된 secret으로 토큰 파싱시 SignatureVerificationException이 발생한다.")
  void shouldThrowExceptionWhenParsingTokenWithWrongSecret() {
    // given
    val tokenInfo = new TokenInfo(1L, false, ADMIN);

    when(jwtSecretGateway.getSecret()).thenReturn("valid_secret", "wrong_secret");
    // when
    val token = jwtUtil.generateToken(tokenInfo);
    // then
    assertThrowsExactly(SignatureVerificationException.class, () -> jwtUtil.parseToken(token));
  }

  @Test
  @DisplayName("만료된 토큰 파싱시 TokenExpiredException이 발생한다.")
  void shouldThrowExceptionWhenParsingExpiredToken() {
    // given
    val secert = "valid_secret";

    val expiredToken = JWT.create()
        .withIssuer("swimcb")
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now())
        .withSubject("1")
        .withClaim("isGuest", false)
        .withClaim("role", ADMIN.name())
        .sign(Algorithm.HMAC256(secert));

    when(jwtSecretGateway.getSecret()).thenReturn(secert);
    // when
    // then
    assertThrowsExactly(TokenExpiredException.class, () -> jwtUtil.parseToken(expiredToken));
  }
}