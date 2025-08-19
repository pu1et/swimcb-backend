package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.DeleteBoSwimmingClassSubTypeUseCase;
import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassSubTypeCommand;
import com.project.swimcb.token.domain.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/class-types/{classTypeId}/class-sub-types/{classSubTypeId}")
@RequiredArgsConstructor
public class DeleteBoSwimmingClassSubTypeController {

  private final DeleteBoSwimmingClassSubTypeUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 강습구분 단건 삭제")
  @DeleteMapping
  public void deleteBoSwimmingClassSubTypes(
      @PathVariable(name = "classTypeId") long classTypeId,
      @PathVariable(name = "classSubTypeId") long classSubTypeId,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.deleteBoSwimmingClassSubType(
        DeleteBoSwimmingClassSubTypeCommand.builder()
            .swimmingPoolId(tokenInfo.swimmingPoolId())
            .swimmingClassTypeId(classTypeId)
            .swimmingClassSubTypeId(classSubTypeId)
            .build()
    );
  }
}

