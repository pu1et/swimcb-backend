package com.project.swimcb.oauth2.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record OAuth2ClientInfo(
    @NonNull Registration registration,
    @NonNull Provider provider
) {

  @Builder
  public record Registration(
      @NonNull String clientId,
      @NonNull String clientSecret,
      @NonNull String redirectUri,
      @NonNull String authorizationGrantType,
      @NonNull List<String> scope
  ) {
  }

  @Builder
  public record Provider(
      @NonNull String authorizationUri
  ) {
  }
}
