package com.project.swimcb.bo.instructor.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/instructors")
@RequiredArgsConstructor
public class UpdateBoInstructorsController {

  @Operation(summary = "클래스 데이터 관리 - 강사 리스트 일괄 업데이트")
  @PutMapping
  public void updateBoSwimmingClassInstructors(
      @Valid @RequestBody UpdateBoInstructorsRequest request
  ) {
  }
}

