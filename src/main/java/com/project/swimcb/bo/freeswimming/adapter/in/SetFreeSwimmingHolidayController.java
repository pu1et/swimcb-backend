package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming/holidays")
@RequiredArgsConstructor
public class SetFreeSwimmingHolidayController {

  @Operation(summary = "자유수영 데이터 관리 - 휴무일 설정")
  @PostMapping
  public void setFreeSwimmingFullyBooked(
      @Valid @RequestBody SetFreeSwimmingHolidayRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
  }

}
