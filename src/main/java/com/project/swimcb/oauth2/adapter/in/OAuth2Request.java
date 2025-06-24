package com.project.swimcb.oauth2.adapter.in;

import com.project.swimcb.oauth2.domain.enums.Environment;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record OAuth2Request(
    @NonNull String code,
    Environment env
) {

}
