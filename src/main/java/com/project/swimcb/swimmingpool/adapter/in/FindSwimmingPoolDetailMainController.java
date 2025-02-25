package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수영장")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/{swimmingPoolId}/main")
public class FindSwimmingPoolDetailMainController {

  @Operation(summary = "수영장 상세 조회 - 메인정보")
  @GetMapping
  public SwimmingPoolDetailMain findSwimmingPoolDetailMain(
      @PathVariable(value = "swimmingPoolId") long swimmingPoolId
  ) {
    return SwimmingPoolDetailMain.builder()
        .imageUrls(List.of("https://ibb.co/bjGKF8WV", "https://ibb.co/67BwJXjC",
            "https://ibb.co/zWdrk20p"))
        .name("올림픽 수영장")
        .isFavorite(false)
        .star("4.5")
        .reviewCount(35)
        .address("서울시 송파구 올림픽로")
        .phoneNumber("02-2180-3745")
        .build();
  }
}
