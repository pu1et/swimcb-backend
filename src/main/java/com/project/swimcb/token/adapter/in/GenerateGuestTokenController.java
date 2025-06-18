package com.project.swimcb.token.adapter.in;

import com.project.swimcb.token.application.in.GenerateAdminTokenUseCase;
import com.project.swimcb.token.application.in.GenerateCustomerTokenUseCase;
import com.project.swimcb.token.application.in.GenerateGuestTokenUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    return generateCustomerTokenUseCase.generateCustomerToken(1L);
  }

  @Operation(summary = "회원-고객 토큰 생성")
  @PostMapping("/member/{memberId}")
  public String generateMemberToken(
      @PathVariable(value = "memberId", required = true) Long memberId
  ) {
    return generateCustomerTokenUseCase.generateCustomerToken(memberId);
  }

  @Operation(summary = "회원-관리자 토큰 생성")
  @PostMapping("/admin")
  public String generateAdminToken(@Valid @RequestBody Body body) {
    return generateAdminTokenUseCase.generateAdminToken(
        body.loginId(),
        body.password()
    );
  }

  @Schema(name = "GenerateAdminTokenBody")
  private record Body(
      @NotNull
      String loginId,

      @NotNull
      String password
  ) {

  }

}
