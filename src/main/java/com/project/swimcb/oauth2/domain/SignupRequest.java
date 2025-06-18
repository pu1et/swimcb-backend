package com.project.swimcb.oauth2.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record SignupRequest(
    @NonNull String name,
    @NonNull String email,
    @NonNull String phoneNumber
) {

}
