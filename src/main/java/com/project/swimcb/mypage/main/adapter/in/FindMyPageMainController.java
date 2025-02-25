package com.project.swimcb.mypage.main.adapter.in;

import com.project.swimcb.mypage.main.domain.MyPageMain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "마이페이지")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/my-page/main")
public class FindMyPageMainController {

  @Operation(summary = "마이페이지 메인 조회")
  @GetMapping
  public MyPageMain findMyPageMain() {
    return MyPageMain.builder()
        .profileImageUrl(null)
        .name("첨부엉")
        .phoneNumber("+82 10-1234-5678")
        .reservationCount(2)
        .build();
  }
}
