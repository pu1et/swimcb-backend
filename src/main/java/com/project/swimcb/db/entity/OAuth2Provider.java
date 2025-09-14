package com.project.swimcb.db.entity;

import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import lombok.NonNull;

public enum OAuth2Provider {
  KAKAO, APPLE;

  public static OAuth2Provider of(@NonNull OAuth2ProviderType providerType) {
    return switch (providerType) {
      case KAKAO -> KAKAO;
      case APPLE -> APPLE;
    };
  }
}
