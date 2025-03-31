package com.project.swimcb.swimmingpool.application.out;

import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;

public interface FindSwimmingClassTicketGateway {

  SwimmingClassTicketInfo findSwimmingClassTicket(long ticketId);
}
