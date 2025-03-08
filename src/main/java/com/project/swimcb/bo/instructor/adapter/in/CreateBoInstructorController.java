package com.project.swimcb.bo.instructor.adapter.in;

import com.project.swimcb.bo.instructor.application.in.CreateBoInstructorUseCase;
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

@Tag(name = "BO - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/instructors")
@RequiredArgsConstructor
public class CreateBoInstructorController {

  private final CreateBoInstructorUseCase useCase;

  @Operation(summary = "클래스 데이터 관리 - 강사 리스트 단건 생성")
  @PostMapping
  public void createBoSwimmingClassInstructor(
      @AuthenticationPrincipal TokenInfo tokenInfo,
      @Valid @RequestBody CreateBoInstructorRequest request
  ) {
    useCase.createBoInstructor(request.toCommand(tokenInfo.swimmingPoolId()));
  }
}

