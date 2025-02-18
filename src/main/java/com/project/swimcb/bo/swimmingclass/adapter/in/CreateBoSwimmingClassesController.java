package com.project.swimcb.bo.swimmingclass.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/swimming-classes")
@RequiredArgsConstructor
public class CreateBoSwimmingClassesController {

  @Operation(summary = "클래스 데이터 관리 - 클래스 추가")
  @PostMapping
  public void createBoSwimmingClass(
      @Valid @RequestBody CreateBoSwimmingClassRequest request
  ) {
  }
}

