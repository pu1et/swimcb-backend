package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingResponse.FreeSwimming;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingResponse.Ticket;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingResponse.Time;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimming;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
class FindSwimmingPoolDetailFreeSwimmingResponseMapper {

  public FindSwimmingPoolDetailFreeSwimmingResponse toResponse(
      @NonNull SwimmingPoolDetailFreeSwimming freeSwimming) {

    val contents = freeSwimming
        .freeSwimmings()
        .stream()
        .map(i -> FreeSwimming.builder()
            .time(
                Time.builder()
                    .startTime(i.time().startTime())
                    .endTime(i.time().endTime())
                    .build()
            )
            .days(i.daysOfWeek().toDays())
            .ticket(
                Ticket.builder()
                    .name(i.ticket().name())
                    .price(i.ticket().price())
                    .build()
            )
            .build()
        ).toList();
    return new FindSwimmingPoolDetailFreeSwimmingResponse(contents);
  }

}
