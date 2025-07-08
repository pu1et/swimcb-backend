package com.project.swimcb.bo.freeswimming.adapter.in;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record SetFreeSwimmingHolidayRequest(

    @NotEmpty(message = "자유수영 일별 상태 ID 리스트는 필수입니다.")
    List<Long> freeSwimmingDayStatusIds,

    @NotNull(message = "휴무일 여부는 null이 될 수 없습니다.")
    Boolean isHoliday
) {

}
