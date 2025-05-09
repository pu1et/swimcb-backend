package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.application.in.UpdateBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicket;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicketRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Ticket;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateBoSwimmingClassInteractor implements UpdateBoSwimmingClassUseCase {

  private final UpdateBoSwimmingClassDsGateway gateway;
  private final SwimmingClassRepository classRepository;
  private final SwimmingClassTicketRepository ticketRepository;

  @Override
  public void updateBoSwimmingClass(@NonNull UpdateBoSwimmingClassCommand request) {
    gateway.updateSwimmingClass(request);
    updateTickets(request.swimmingPoolId(), request.swimmingClassId(), request.tickets());
  }

  private void updateTickets(long swimmingPoolId, long swimmingClassId,
      @NonNull List<Ticket> tickets) {

    val swimmingClass = classRepository.findBySwimmingPool_IdAndId(swimmingPoolId, swimmingClassId)
        .orElseThrow(() -> new NoSuchElementException("클래스가 존재하지 않습니다."));

    gateway.deleteAllTicketsBySwimmingClassId(swimmingClassId);

    val newTickets = tickets.stream()
        .map(i -> SwimmingClassTicket.create(swimmingClass, i.name(), i.price()))
        .toList();
    ticketRepository.saveAll(newTickets);
  }
}
