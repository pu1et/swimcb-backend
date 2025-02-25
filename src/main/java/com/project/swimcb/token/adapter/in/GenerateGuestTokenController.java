package com.project.swimcb.token.adapter.in;

import com.project.swimcb.token.application.in.GenerateAdminTokenUseCase;
import com.project.swimcb.token.application.in.GenerateCustomerTokenUseCase;
import com.project.swimcb.token.application.in.GenerateGuestTokenUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰")
@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class GenerateGuestTokenController {

  private final GenerateGuestTokenUseCase generateGuestTokenUseCase;
  private final GenerateCustomerTokenUseCase generateCustomerTokenUseCase;
  private final GenerateAdminTokenUseCase generateAdminTokenUseCase;

  @Operation(summary = "게스트 토큰 생성")
  @PostMapping("/guest")
  public String generateGuestToken() {
    return generateGuestTokenUseCase.generateGuestToken();
  }

  @Operation(summary = "회원-고객 토큰 생성")
  @PostMapping("/customer")
  public String generateCustomerToken() {
    return generateCustomerTokenUseCase.generateCustomerToken();
  }

  @Operation(summary = "회원-관리자 토큰 생성")
  @PostMapping("/admin")
  public String generateAdminToken() {
    return generateAdminTokenUseCase.generateAdminToken();
  }
}
