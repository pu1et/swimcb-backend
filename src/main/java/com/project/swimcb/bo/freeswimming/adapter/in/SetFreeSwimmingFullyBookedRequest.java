package com.project.swimcb.bo.freeswimming.adapter.in;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Builder;

@Builder
public record SetFreeSwimmingFullyBookedRequest(

    @NotEmpty(message = "자유수영 ID 리스트는 필수입니다.")
    List<Long> freeSwimmingIds,

    @NotNull(message = "예약마감 여부는 null이 될 수 없습니다.")
    Boolean isFullyBooked
) {

}
