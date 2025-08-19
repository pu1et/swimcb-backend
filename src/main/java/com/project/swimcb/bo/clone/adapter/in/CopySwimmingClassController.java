package com.project.swimcb.bo.clone.adapter.in;

import com.project.swimcb.bo.clone.application.port.in.CopySwimmingClassUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/copy")
@RequiredArgsConstructor
public class CopySwimmingClassController {

  private final CopySwimmingClassUseCase useCase;

  @Operation(summary = "전체 수영장 클래스 복사")
  @PostMapping
  public void copySwimmingClassSchedules(
      @Valid @RequestBody CopySwimmingClassRequest request
  ) {
    useCase.copySwimmingClass(request.toCommand());
  }
}
