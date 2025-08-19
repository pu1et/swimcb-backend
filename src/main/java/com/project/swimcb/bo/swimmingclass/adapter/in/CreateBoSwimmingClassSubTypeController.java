package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassSubTypeUseCase;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/class-types/{classTypeId}/class-sub-types")
@RequiredArgsConstructor
public class CreateBoSwimmingClassSubTypeController {

  private final CreateBoSwimmingClassSubTypeUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 강습구분 단건 추가")
  @PostMapping
  public void createBoSwimmingClassSubTypes(
      @PathVariable(name = "classTypeId") long classTypeId,
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody CreateBoSwimmingClassSubTypeRequest request
  ) {
    useCase.createBoSwimmingClassSubType(
        request.toCommand(tokenInfo.swimmingPoolId(), classTypeId));
  }
}

