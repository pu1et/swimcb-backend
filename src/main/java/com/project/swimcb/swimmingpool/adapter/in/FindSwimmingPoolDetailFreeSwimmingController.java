package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming.FreeSwimming;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/free-swimming")
public class FindSwimmingPoolDetailFreeSwimmingController {

  @Operation(summary = "수영장 상세 조회 - 자유수영")
  @GetMapping
  public SwimmingPoolDetailFreeSwimming findSwimmingPoolDetailFreeSwimming(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId,
      @Parameter(description = "날짜", example = "2025-01-01T01:01:01") @RequestParam(value = "date-time") LocalDateTime dateTime
  ) {

    return SwimmingPoolDetailFreeSwimming.builder()
        .imageUrl("https://ibb.co/bjGKF8WV")
        .freeSwimming(
            FreeSwimming.builder()
                .freeSwimmingId(1L)
                .name("일일권")
                .price(5000)
                .isReservable(true)
                .build()
        )
        .build();
  }
}
