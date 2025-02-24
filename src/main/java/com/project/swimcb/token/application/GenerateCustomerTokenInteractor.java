package com.project.swimcb.token.application;

import com.project.swimcb.token.application.in.GenerateCustomerTokenUseCase;
import com.project.swimcb.token.application.in.GenerateGuestTokenUseCase;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.domain.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateCustomerTokenInteractor implements GenerateCustomerTokenUseCase {

  private final JwtPort jwtPort;

  @Override
  public String generateCustomerToken() {
    return jwtPort.generateToken(TokenInfo.customer(1L));
  }
}
