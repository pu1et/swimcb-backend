package com.project.swimcb.bo.instructor.adapter.in;

import com.project.swimcb.bo.instructor.application.in.DeleteBoInstructorUseCase;
import com.project.swimcb.bo.instructor.domain.DeleteBoInstructorCommand;
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
@RequestMapping("/api/bo/instructors/{instructorId}")
@RequiredArgsConstructor
public class DeleteBoInstructorController {

  private final DeleteBoInstructorUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 강사 리스트 단건 삭제")
  @DeleteMapping
  public void deleteBoSwimmingClassInstructor(
      @PathVariable(name = "instructorId") long instructorId,
      @AuthenticationPrincipal TokenInfo tokenInfo
  ) {
    useCase.deleteBoInstructor(
        new DeleteBoInstructorCommand(tokenInfo.swimmingPoolId(), instructorId));
  }
}

