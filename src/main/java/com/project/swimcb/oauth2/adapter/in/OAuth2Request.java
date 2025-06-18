package com.project.swimcb.oauth2.adapter.in;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record OAuth2Request(
    @NonNull String code
) {

}
