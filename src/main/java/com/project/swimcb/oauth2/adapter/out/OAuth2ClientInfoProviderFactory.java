package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.application.port.in.OAuth2ClientInfoProvider;
import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ClientInfoProviderFactory {

  private final OAuth2ClientInfoProvider kakaoOAuth2ClientInfoProvider;

  public OAuth2ClientInfoProvider getProvider(@NonNull OAuth2ProviderType type) {
    return switch (type) {
      case KAKAO -> kakaoOAuth2ClientInfoProvider;
    };
  }

}
