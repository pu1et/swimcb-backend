package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingDetailResponse.FreeSwimming;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingDetailResponse.Ticket;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFreeSwimmingDetailResponse.Time;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
class FindSwimmingPoolDetailFreeSwimmingDetailResponseMapper {

  public FindSwimmingPoolDetailFreeSwimmingDetailResponse toResponse(
      @NonNull SwimmingPoolDetailFreeSwimmingDetail detail
  ) {
    val contents = detail.freeSwimmings()
        .stream()
        .map(i -> FreeSwimming.builder()
            .dayStatusId(i.dayStatusId())
            .time(
                Time.builder()
                    .startTime(i.time().startTime())
                    .endTime(i.time().endTime())
                    .build()
            )
            .minTicketPrice(i.minTicketPrice())
            .tickets(
                i.tickets()
                    .stream()
                    .map(j -> Ticket.builder()
                        .id(j.id())
                        .name(j.name())
                        .price(j.price())
                        .build()
                    ).toList()
            )
            .favoriteId(i.favoriteId())
            .build())
        .toList();
    return new FindSwimmingPoolDetailFreeSwimmingDetailResponse(contents);
  }

}
