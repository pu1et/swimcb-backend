package com.project.swimcb.bo.freeswimming.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming")
@RequiredArgsConstructor
public class CreateBoFreeSwimmingController {

  @Operation(summary = "자유수영 데이터 관리 - 자유수영 추가")
  @PostMapping
  public void createBoFreeSwimming(
      @Valid @RequestBody CreateBoFreeSwimmingRequest request
  ) {
  }
}
