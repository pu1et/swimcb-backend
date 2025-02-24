package com.project.swimcb.config.security;

import javax.security.sasl.AuthenticationException;

public class MissingAuthorizationHeaderException extends AuthenticationException {

  public MissingAuthorizationHeaderException(String message) {
    super(message);
  }
}
