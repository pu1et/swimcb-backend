package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;

public interface FindSwimmingClassTicketUseCase {

  SwimmingClassTicketInfo findSwimmingClassTicket(long ticketId);
}
