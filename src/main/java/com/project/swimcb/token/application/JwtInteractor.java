package com.project.swimcb.token.application;

import static java.time.temporal.ChronoUnit.DAYS;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.application.out.JwtSecretGateway;
import com.project.swimcb.token.domain.TokenInfo;
import java.time.Instant;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtInteractor implements JwtPort {

  private final JwtSecretGateway jwtSecretGateway;

  @Override
  public String generateToken(@NonNull TokenInfo tokenInfo) {
    return JWT.create()
        .withIssuer("swimcb")
        .withIssuedAt(Instant.now())
        .withExpiresAt(Instant.now().plus(30, DAYS))
        .withSubject(String.valueOf(tokenInfo.memberId()))
        .withClaim("isGuest", tokenInfo.isGuest())
        .withClaim("role", tokenInfo.role().name())
        .sign(Algorithm.HMAC256(secret()));
  }

  @Override
  public DecodedJWT parseToken(@NonNull String token) {
    return JWT.require(Algorithm.HMAC256(secret()))
        .withIssuer("swimcb")
        .build()
        .verify(token);
  }

  private String secret() {
    return jwtSecretGateway.getSecret();
  }
}
