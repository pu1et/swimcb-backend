package com.project.swimcb.oauth2.adapter.out.exception;

import lombok.NonNull;

public class OAuth2TokenRequestException extends RuntimeException{

  public OAuth2TokenRequestException(@NonNull String messsage) {
    super(messsage);
  }

}
