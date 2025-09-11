package com.project.swimcb.oauth2.application;

import com.project.swimcb.oauth2.application.port.in.OAuth2ClientInfoProvider;
import com.project.swimcb.oauth2.domain.OAuth2ClientInfo;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("appleOAuth2ClientInfoProvider")
class AppleOAuth2ClientInfoProvider implements OAuth2ClientInfoProvider {

  @Value("${spring.security.oauth2.client.registration.apple.client-id}")
  private String clientId;

  @Value("${spring.security.oauth2.client.registration.apple.redirect-uri}")
  private String redirectUri;

  @Value("${spring.security.oauth2.client.registration.apple.scope}")
  private String[] scope;

  @Value("${spring.security.oauth2.client.provider.apple.authorization-uri}")
  private String authorizationUri;

  @Value("${spring.security.oauth2.client.provider.apple.token-uri}")
  private String tokenUri;

  @Override
  public OAuth2ClientInfo getOAuth2ClientInfo() {
    return OAuth2ClientInfo.builder()
        .registration(
            OAuth2ClientInfo.Registration.builder()
                .clientId(clientId)
                .redirectUri(redirectUri)
                .scope(List.of(scope))
                .build()
        )
        .provider(
            OAuth2ClientInfo.Provider.builder()
                .authorizationUri(authorizationUri)
                .tokenUri(tokenUri)
                .memberInfoUri("")
                .build()
        )
        .build();
  }

}
