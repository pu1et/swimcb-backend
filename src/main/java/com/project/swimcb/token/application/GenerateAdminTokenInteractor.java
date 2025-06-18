package com.project.swimcb.token.application;

import com.project.swimcb.db.repository.AdminRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import com.project.swimcb.token.application.in.GenerateAdminTokenUseCase;
import com.project.swimcb.token.application.in.JwtPort;
import com.project.swimcb.token.domain.TokenInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateAdminTokenInteractor implements GenerateAdminTokenUseCase {

  private final JwtPort jwtPort;
  private final AdminRepository adminRepository;
  private final SwimmingPoolRepository swimmingPoolRepository;

  @Override
  public String generateAdminToken(@NonNull String loginId, @NonNull String password) {
    val admin = adminRepository.findByLoginId(loginId)
        .orElseThrow(() -> new IllegalArgumentException("계정이 존재하지 않습니다."));
    val swimmingPool = swimmingPoolRepository.findByAdminEntity_Id(admin.getId())
        .orElseThrow(() -> new IllegalArgumentException("수영장이 존재하지 않습니다."));
    return jwtPort.generateToken(TokenInfo.admin(admin.getId(), swimmingPool.getId()));
  }

}
