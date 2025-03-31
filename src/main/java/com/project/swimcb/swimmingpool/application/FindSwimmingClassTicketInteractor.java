package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingClassTicketUseCase;
import com.project.swimcb.swimmingpool.application.out.FindSwimmingClassTicketGateway;
import com.project.swimcb.swimmingpool.domain.SwimmingClassTicketInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindSwimmingClassTicketInteractor implements FindSwimmingClassTicketUseCase {

  private final FindSwimmingClassTicketGateway gateway;

  @Override
  public SwimmingClassTicketInfo findSwimmingClassTicket(long ticketId) {
    return gateway.findSwimmingClassTicket(ticketId);
  }
}
