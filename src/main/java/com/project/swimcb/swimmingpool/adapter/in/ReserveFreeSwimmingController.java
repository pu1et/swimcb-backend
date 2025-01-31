package com.project.swimcb.swimmingpool.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "자유수영")
@RestController
@RequestMapping("/api/free-swimming/{freeSwimmingId}/reservations")
public class ReserveFreeSwimmingController {

  @Operation(summary = "자유수영 예약")
  @PostMapping
  public void reserveFreeSwimming(
      @PathVariable(value = "freeSwimmingId") long freeSwimmingId
  ) {
  }
}
