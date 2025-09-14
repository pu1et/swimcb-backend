package com.project.swimcb.oauth2.adapter.out;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.swimcb.oauth2.application.port.in.OAuth2ClientInfoProvider;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppleIdTokenParser {

  private final OAuth2ClientInfoProvider appleOAuth2ClientInfoProvider;

  public Map<String, Object> parseAndVerifyToken(@NonNull String idToken) {

    try {
      // 1. idToken 파싱
      val signedJWT = SignedJWT.parse(idToken);

      // 2. Apple 공개키 세트 로드
      val jwkSet = JWKSet.load(URI.create("https://appleid.apple.com/auth/keys").toURL());

      // 3. kid(header에서)를 이용해 맞는 키 찾기
      val keyID = signedJWT.getHeader().getKeyID();
      val jwk = jwkSet.getKeyByKeyId(keyID);
      if (jwk == null) {
        throw new IllegalArgumentException(
            "invalid token : unknown key id ==> key id : " + keyID + ", idToken : " + idToken);
      }

      if (!(jwk instanceof RSAKey)) {
        throw new IllegalArgumentException(
            "key with keyID " + keyID + " is not RSAKey but " + jwk.getClass()
        );
      }

      // 4. 공개키 얻기
      val publicKey = ((RSAKey) jwk).toRSAPublicKey();

      // 5. 서명 검증
      val verifier = new RSASSAVerifier(publicKey);
      if (!signedJWT.verify(verifier)) {
        throw new IllegalArgumentException(
            "invalid token : signature verification failed  ==> verifier : " + verifier
                + ", idToken : " + idToken
        );
      }

      // 6. 클레임 추출
      final var claims = getJwtClaimsSet(idToken, signedJWT);

      return claims.getClaims();

    } catch (ParseException | MalformedURLException e) {
      throw new IllegalArgumentException("invalid token : " + e);
    } catch (IOException | JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  private JWTClaimsSet getJwtClaimsSet(
      @NonNull String idToken,
      @NonNull SignedJWT signedJWT
  ) throws ParseException {

    val clientId = appleOAuth2ClientInfoProvider.getOAuth2ClientInfo().registration().clientId();

    // 필수 클레임 검증
    val claims = signedJWT.getJWTClaimsSet();
    if (!"https://appleid.apple.com".equals(claims.getIssuer())) {
      throw new IllegalArgumentException(
          "invalid token : issuer is not apple ==> issuer : " + claims.getIssuer() + ", idToken : "
              + idToken);
    }
    if (!claims.getAudience().contains(clientId)) {
      throw new IllegalArgumentException(
          "invalid token : audience is not " + clientId + " ==> audience : " + claims.getAudience()
              + ", idToken : " + idToken
      );
    }

    if (claims.getExpirationTime().before(new Date())) {
      throw new IllegalArgumentException(
          "invalid token : expired token ==> idToken : " + idToken
      );
    }
    return claims;
  }

}
