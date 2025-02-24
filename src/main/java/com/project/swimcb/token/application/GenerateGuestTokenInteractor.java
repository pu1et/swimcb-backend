package com.project.swimcb.token.application;

import com.project.swimcb.token.application.in.GenerateGuestTokenUseCase;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.domain.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateGuestTokenInteractor implements GenerateGuestTokenUseCase {

  private final JwtPort jwtPort;

  @Override
  public String generateGuestToken() {
    return jwtPort.generateToken(TokenInfo.guest());
  }
}
