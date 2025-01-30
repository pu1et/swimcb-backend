package com.project.swimcb.swimmingpool.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "클래스")
@RestController
@RequestMapping("/api/swimming-classes/{swimmingClassId}/reservations")
public class ReserveSwimmingClassController {

  @Operation(summary = "수영 클래스 예약")
  @PostMapping
  public void reserveSwimmingClass(@RequestBody ReserveSwimmingClassRequest request) {
  }
}
