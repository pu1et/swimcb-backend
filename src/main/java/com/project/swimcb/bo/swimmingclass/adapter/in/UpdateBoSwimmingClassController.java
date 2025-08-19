package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.UpdateBoSwimmingClassUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/{swimmingClassId}")
@RequiredArgsConstructor
public class UpdateBoSwimmingClassController {

  private final UpdateBoSwimmingClassUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 수정")
  @PutMapping
  public void updateBoSwimmingClasses(
      @PathVariable(name = "swimmingClassId") long swimmingClassId,
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody UpdateBoSwimmingClassRequest request
  ) {
    useCase.updateBoSwimmingClass(request.toCommand(tokenInfo.swimmingPoolId(), swimmingClassId));
  }
}

