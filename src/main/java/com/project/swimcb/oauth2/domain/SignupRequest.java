package com.project.swimcb.oauth2.domain;

import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SignupRequest(
    String name,
    @NonNull String email,
    String phoneNumber,
    @NonNull OAuth2ProviderType providerType
) {

}
