package com.project.swimcb.bo.instructor.adapter.in;

import com.project.swimcb.bo.instructor.application.in.UpdateBoInstructorUseCase;
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
@RequestMapping("/api/bo/instructors/{instructorId}")
@RequiredArgsConstructor
public class UpdateBoInstructorController {

  private final UpdateBoInstructorUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 강사 리스트 단건 업데이트")
  @PutMapping
  public void updateBoSwimmingClassInstructor(
      @PathVariable(name = "instructorId") long instructorId,
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody UpdateBoInstructorRequest request
  ) {
    useCase.updateBoInstructor(request.toCommand(tokenInfo.swimmingPoolId(), instructorId));
  }
}

