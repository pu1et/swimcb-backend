package com.project.swimcb.bo.freeswimming.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming")
@RequiredArgsConstructor
public class DeleteBoFreeSwimmingController {

  @Operation(summary = "자유수영 데이터 관리 - 자유수영 삭제")
  @DeleteMapping
  public void deleteBoFreeSwimming(
      @RequestBody DeleteBoFreeSwimmingRequest request
  ) {
  }
}

