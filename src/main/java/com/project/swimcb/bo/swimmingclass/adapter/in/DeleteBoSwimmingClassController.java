package com.project.swimcb.bo.swimmingclass.adapter.in;

import com.project.swimcb.bo.swimmingclass.application.in.DeleteBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassCommand;
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

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-classes/{swimmingClassId}")
@RequiredArgsConstructor
public class DeleteBoSwimmingClassController {

  private final DeleteBoSwimmingClassUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 클래스 삭제")
  @DeleteMapping
  public void deleteBoSwimmingClasses(
      @PathVariable(name = "swimmingClassId") long swimmingClassId,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.deleteBoSwimmingClass(DeleteBoSwimmingClassCommand.builder()
        .swimmingPoolId(tokenInfo.swimmingPoolId())
        .swimmingClassId(swimmingClassId)
        .build());
  }
}

