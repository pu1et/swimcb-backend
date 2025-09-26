package com.project.swimcb.oauth2.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record OAuth2Member(
    String name,
    @NonNull String email,
    String phoneNumber
) {

}
