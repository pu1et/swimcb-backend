package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;
import lombok.NonNull;

public interface FindSwimmingClassTicketResponseMapper {

  FindSwimmingClassTicketResponse toResponse(@NonNull SwimmingClassTicketInfo ticketInfo);
}