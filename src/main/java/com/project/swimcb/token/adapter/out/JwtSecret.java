package com.project.swimcb.token.adapter.out;

import com.project.swimcb.token.application.out.JwtSecretGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtSecret implements JwtSecretGateway {

  @Value("${jwt.secret}")
  private String secret;

  @Override
  public String getSecret() {
    return secret;
  }
}
