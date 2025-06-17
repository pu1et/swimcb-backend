package com.project.swimcb.oauth2.domain.enums;

import java.util.Arrays;
import lombok.NonNull;

public enum OAuth2ProviderType {
  KAKAO;

  public static OAuth2ProviderType from(@NonNull String providerType) {
    return Arrays.stream(values())
        .filter(type -> type.name().equalsIgnoreCase(providerType))
        .findFirst()
        .orElseThrow(
            () -> new IllegalArgumentException("Invalid OAuth2 provider type: " + providerType));
  }
}
