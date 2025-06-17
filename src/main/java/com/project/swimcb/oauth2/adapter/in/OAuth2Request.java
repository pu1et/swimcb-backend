package com.project.swimcb.oauth2.adapter.in;

import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record OAuth2Request(
    @NonNull OAuth2ProviderType providerType,
    @NonNull String code
) {

}
