package com.project.swimcb.bo.swimmingclass.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/swimming-classes")
@RequiredArgsConstructor
public class DeleteBoSwimmingClassController {

  @Operation(summary = "클래스 데이터 관리 - 클래스 삭제")
  @DeleteMapping
  public void deleteBoSwimmingClasses(
      @RequestBody DeleteBoSwimmingClassRequest request
  ) {
  }
}

