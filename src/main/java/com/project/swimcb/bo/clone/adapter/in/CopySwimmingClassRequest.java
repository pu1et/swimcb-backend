package com.project.swimcb.bo.clone.adapter.in;

import com.project.swimcb.bo.clone.domain.CopySwimmingClassCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.YearMonth;

@Schema(description = "수영장 클래스 복사 요청")
public record CopySwimmingClassRequest(
    @Schema(description = "원본 월", example = "2025-07")
    @NotNull(message = "fromMonth는 null일 수 없습니다.")
    YearMonth fromMonth,

    @Schema(description = "복사 월", example = "2025-12")
    @NotNull(message = "toMonth는 null일 수 없습니다.")
    YearMonth toMonth
) {

  public CopySwimmingClassCommand toCommand() {
    return new CopySwimmingClassCommand(fromMonth, toMonth);
  }

}
