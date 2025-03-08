package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.UpdateBoSwimmingClassSubTypeUseCase;
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

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/class-types/{classTypeId}/class-sub-types/{classSubTypeId}")
@RequiredArgsConstructor
public class UpdateBoSwimmingClassSubTypeController {

  private final UpdateBoSwimmingClassSubTypeUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 강습구분 단건 업데이트")
  @PutMapping
  public void updateBoSwimmingClassSubTypes(
      @PathVariable(name = "classTypeId") long classTypeId,
      @PathVariable(name = "classSubTypeId") long classSubTypeId,
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody UpdateBoSwimmingClassSubTypeRequest request
  ) {
    useCase.updateBoSwimmingClassSubType(
        request.toCommand(tokenInfo.swimmingPoolId(), classTypeId, classSubTypeId));
  }
}

