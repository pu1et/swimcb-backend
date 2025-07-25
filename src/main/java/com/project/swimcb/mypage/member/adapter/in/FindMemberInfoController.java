package com.project.swimcb.mypage.member.adapter.in;

import com.project.swimcb.mypage.member.application.port.in.FindMemberInfoUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/members/me")
@RequiredArgsConstructor
public class FindMemberInfoController {

  private final FindMemberInfoUseCase useCase;

  @Operation(summary = "마이페이지 회원 정보 조회")
  @GetMapping
  public FindMemberInfoResponse findMemberInfo(
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    val memberInfo = useCase.findMemberInfo(tokenInfo.memberId());
    return FindMemberInfoResponse.builder()
        .name(memberInfo.name())
        .phone(memberInfo.phone())
        .email(memberInfo.email())
        .build();
  }

}
