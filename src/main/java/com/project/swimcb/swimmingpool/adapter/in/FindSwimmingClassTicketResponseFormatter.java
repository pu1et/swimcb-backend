package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassTicketResponse.SwimmingClass;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassTicketResponse.SwimmingClassTicket;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
class FindSwimmingClassTicketResponseFormatter implements
    FindSwimmingClassTicketResponseMapper {

  @Override
  public FindSwimmingClassTicketResponse toResponse(@NonNull SwimmingClassTicketInfo ticketInfo) {
    val swimmingClass = ticketInfo.swimmingClass();
    val ticket = ticketInfo.ticket();
    return FindSwimmingClassTicketResponse.builder()
        .swimmingClass(
            SwimmingClass.builder()
                .type(swimmingClass.type())
                .subType(swimmingClass.subType())
                .days(swimmingClass.days())
                .startTime(swimmingClass.startTime())
                .endTime(swimmingClass.endTime())
                .build()
        )
        .ticket(
            SwimmingClassTicket.builder()
                .name(ticket.name())
                .price(ticket.price())
                .status(ticket.status())
                .build()
        )
        .build();
  }
}
