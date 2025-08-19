package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.CancelBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.CancelBoSwimmingClassCommand;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/{swimmingClassId}/cancel")
@RequiredArgsConstructor
public class CancelBoSwimmingClassController {

  private final CancelBoSwimmingClassUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 폐강 처리")
  @PatchMapping
  public void deleteBoSwimmingClasses(
      @PathVariable(name = "swimmingClassId") Long swimmingClassId,
      @Valid @RequestBody CancelBoSwimmingClassRequest request,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.cancelBoSwimmingClass(CancelBoSwimmingClassCommand.builder()
        .swimmingPoolId(tokenInfo.swimmingPoolId())
        .swimmingClassId(swimmingClassId)
        .cancelReason(request.cancelReason())
        .build());
  }

  @Schema(name = "CancelBoSwimmingClassRequest")
  private record CancelBoSwimmingClassRequest(
      @NotNull
      @Schema(description = "폐강 사유", example = "강사 사정으로 인한 폐강")
      String cancelReason
  ) {

  }

}

