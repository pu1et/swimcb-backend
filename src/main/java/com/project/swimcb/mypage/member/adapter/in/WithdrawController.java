package com.project.swimcb.mypage.member.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@RestController
@RequestMapping("/api/my-page/members/me")
public class WithdrawController {

  @Operation(summary = "마이페이지 회원 탈퇴")
  public void withdraw() {
  }
}
