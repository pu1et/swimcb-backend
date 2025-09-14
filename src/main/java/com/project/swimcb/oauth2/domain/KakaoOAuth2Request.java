package com.project.swimcb.oauth2.domain;

import com.project.swimcb.oauth2.domain.enums.Environment;
import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record KakaoOAuth2Request(
    @NonNull String code,
    Environment env
) implements OAuth2Request {

  @Override
  public OAuth2ProviderType getProvider() {
    return OAuth2ProviderType.KAKAO;
  }

}
