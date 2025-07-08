package com.project.swimcb.bo.freeswimming.adapter.in;

import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming/{freeSwimmingId}/cancel")
@RequiredArgsConstructor
public class CancelBoFreeSwimmingController {

  @Operation(summary = "자유수영 데이터 관리 - 폐강 처리")
  @PatchMapping
  public void cancel(
      @PathVariable(name = "freeSwimmingId") Long freeSwimmingId,
      @Valid @RequestBody CancelBoFreeSwimmingRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
  }

}
