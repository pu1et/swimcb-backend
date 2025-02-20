package com.project.swimcb.bo.swimmingclass.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/swimming-classes/class-types")
@RequiredArgsConstructor
public class UpdateBoSwimmingClassTypesController {

  @Operation(summary = "클래스 데이터 관리 - 클래스 강습형태/구분 리스트 일괄 업데이트")
  @PutMapping
  public void updateBoSwimmingClassTypes(
      @Valid @RequestBody UpdateBoSwimmingClassTypesRequest request
  ) {
  }
}

