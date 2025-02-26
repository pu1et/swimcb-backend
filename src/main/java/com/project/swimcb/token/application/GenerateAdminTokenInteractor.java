package com.project.swimcb.token.application;

import com.project.swimcb.token.application.in.GenerateAdminTokenUseCase;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.domain.TokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateAdminTokenInteractor implements GenerateAdminTokenUseCase {

  private final JwtPort jwtPort;

  @Override
  public String generateAdminToken() {
    return jwtPort.generateToken(TokenInfo.admin(1L, 1L));
  }
}
