package com.project.swimcb.oauth2.domain;

import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import lombok.NonNull;

public record AppleOAuth2Request(
    @NonNull String email
) implements OAuth2Request {

  @Override
  public OAuth2ProviderType getProvider() {
    return OAuth2ProviderType.APPLE;
  }

}
