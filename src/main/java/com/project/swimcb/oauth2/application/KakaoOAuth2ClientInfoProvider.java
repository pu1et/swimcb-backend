package com.project.swimcb.oauth2.application;

import com.project.swimcb.oauth2.domain.OAuth2ClientInfo;
import com.project.swimcb.oauth2.application.port.in.OAuth2ClientInfoProvider;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class KakaoOAuth2ClientInfoProvider implements OAuth2ClientInfoProvider {

  @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
  private String clientSecret;

  @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
  private String redirectUri;

  @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
  private String authorizationGrantType;

  @Value("${spring.security.oauth2.client.registration.kakao.scope}")
  private String[] scope;

  @Value("${spring.security.oauth2.client.provider.kakao.authorization-uri}")
  private String authorizationUri;

  @Override
  public OAuth2ClientInfo getOAuth2ClientInfo() {
    return OAuth2ClientInfo.builder()
        .registration(
            OAuth2ClientInfo.Registration.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(redirectUri)
                .authorizationGrantType(authorizationGrantType)
                .scope(List.of(scope))
                .build()
        )
        .provider(
            OAuth2ClientInfo.Provider.builder()
                .authorizationUri(authorizationUri)
                .build()
        )
        .build();
  }

}
