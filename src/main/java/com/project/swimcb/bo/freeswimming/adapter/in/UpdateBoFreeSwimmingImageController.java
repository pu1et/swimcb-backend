package com.project.swimcb.bo.freeswimming.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/free-swimming/images")
@RequiredArgsConstructor
public class UpdateBoFreeSwimmingImageController {

  @Operation(summary = "자유수영 데이터 관리 - 자유수영 이미지 업데이트")
  @PutMapping
  public void updateFreeSwimmingImage(@Valid @RequestBody UpdateBoFreeSwimmingImageRequest request) {
  }
}
