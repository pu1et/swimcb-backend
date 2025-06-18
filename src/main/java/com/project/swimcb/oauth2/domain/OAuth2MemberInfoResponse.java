package com.project.swimcb.oauth2.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuth2MemberInfoResponse(
    @JsonProperty("id")
    String id,

    @JsonProperty("kakao_account")
    OAuth2KakaoAccount kakaoAccount
) {

  public record OAuth2KakaoAccount(
      @JsonProperty("name")
      String name,

      @JsonProperty("email")
      String email,

      @JsonProperty("phone_number")
      String phoneNumber
  ) {

  }

}
