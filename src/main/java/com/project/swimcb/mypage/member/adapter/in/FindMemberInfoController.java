package com.project.swimcb.mypage.member.adapter.in;

import com.project.swimcb.mypage.member.domain.MemberInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/members/me")
public class FindMemberInfoController {

  @Operation(summary = "마이페이지 회원 정보 조회")
  @GetMapping
  public MemberInfo findMemberInfo() {
    return MemberInfo.builder()
        .name("첨부엉")
        .phoneNumber("+82 10-1234-5678")
        .email("swim-cb@gmail.com")
        .build();
  }
}
