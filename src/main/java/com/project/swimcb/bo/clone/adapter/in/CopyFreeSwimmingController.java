package com.project.swimcb.bo.clone.adapter.in;

import com.project.swimcb.bo.clone.application.port.in.CopyFreeSwimmingUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/free-swimming/copy")
@RequiredArgsConstructor
public class CopyFreeSwimmingController {

  private final CopyFreeSwimmingUseCase useCase;

  @Operation(summary = "전체 자유수영 복사")
  @PostMapping
  public void copyFreeSwimming(
      @Valid @RequestBody CopyFreeSwimmingRequest request
  ) {
    useCase.copyFreeSwimming(request.toCommand());
  }
}
