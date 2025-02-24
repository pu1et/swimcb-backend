package com.project.swimcb.token.adapter.in;

import com.project.swimcb.token.application.in.GenerateGuestTokenUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "토큰")
@RestController
@RequestMapping("/api/token/guest")
@RequiredArgsConstructor
public class GenerateGuestTokenController {

  private final GenerateGuestTokenUseCase generateGuestTokenUseCase;

  @Operation(summary = "게스트 토큰 생성")
  @PostMapping
  public String generateGuestToken() {
    return generateGuestTokenUseCase.generateGuestToken();
  }
}
