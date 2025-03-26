package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

@Builder
public record FindSwimmingPoolDetailClassesCondition(
    long swimmingPoolId,
    @NonNull LocalDate startDate,
    @NonNull LocalDate endDate,
    @NonNull List<LocalTime> startTimes,
    @NonNull List<DayOfWeek> days,
    @NonNull List<SwimmingClassTypeName> classTypes,
    @NonNull List<GroupFixedClassSubTypeName> classSubTypes,
    @NonNull Pageable pageable,
    Long memberId
) {

}
