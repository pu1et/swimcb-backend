package com.project.swimcb.mypage.member.adapter.in;

import com.project.swimcb.mypage.member.application.port.in.WithdrawUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/members/me")
@RequiredArgsConstructor
public class WithdrawController {

  private final WithdrawUseCase useCase;

  @Operation(summary = "마이페이지 회원 탈퇴")
  @DeleteMapping
  public void withdraw(
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.withdraw(tokenInfo.memberId());
  }

}
