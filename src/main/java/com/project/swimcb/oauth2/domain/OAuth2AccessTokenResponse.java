package com.project.swimcb.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuth2AccessTokenResponse(
    @JsonProperty("token_type")
    String tokenType,

    @JsonProperty("access_token")
    String accessToken,

    @JsonProperty("expires_in")
    Long expiresIn,

    @JsonProperty("refresh_token")
    String refreshToken,

    @JsonProperty("refresh_expires_in")
    Long refreshExpiresIn,

    @JsonProperty("scope")
    String scope
) {

}
