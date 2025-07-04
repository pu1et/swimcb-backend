package com.project.swimcb.bo.freeswimming.adapter.out;

import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.FreeSwimming;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.Lifeguard;
import com.project.swimcb.bo.freeswimming.adapter.in.FindBoFreeSwimmingResponse.Time;
import com.project.swimcb.bo.freeswimming.domain.BoFreeSwimming;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class FindBoFreeSwimmingResponseMapper {

  public FindBoFreeSwimmingResponse toResponse(@NonNull List<BoFreeSwimming> freeSwimmings) {
    val result = freeSwimmings
        .stream()
        .map(i -> FreeSwimming.builder()
            .freeSwimmingId(i.freeSwimmingId())
            .days(i.days())
            .time(Time.builder()
                .startTime(i.time().startTime())
                .endTime(i.time().endTime())
                .build())
            .lifeguard(Optional.ofNullable(i.lifeguard())
                .map(j -> Lifeguard.builder()
                    .id(j.id())
                    .name(j.name())
                    .build())
                .orElse(null))
            .ticketPriceRange(
                FindBoFreeSwimmingResponse.TicketPriceRange.builder()
                    .minimumPrice(i.ticketPriceRange().minimumPrice())
                    .maximumPrice(i.ticketPriceRange().maximumPrice())
                    .build()
            )
            .tickets(i.tickets()
                .stream()
                .map(j -> FindBoFreeSwimmingResponse.Ticket.builder()
                    .id(j.id())
                    .name(j.name())
                    .price(j.price())
                    .build())
                .toList())
            .capacity(i.capacity())
            .isExposed(i.isExposed())
            .build()
        ).toList();
    return new FindBoFreeSwimmingResponse(result);
  }
}
