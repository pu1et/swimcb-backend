package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassUseCase;
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
@RequestMapping("/api/bo/swimming-classes")
@RequiredArgsConstructor
public class CreateBoSwimmingClassController {

  private final CreateBoSwimmingClassUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 추가")
  @PostMapping
  public void createBoSwimmingClass(
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody CreateBoSwimmingClassRequest request
  ) {
    useCase.createBoSwimmingClass(request.toCommand(tokenInfo.swimmingPoolId()));
  }
}

