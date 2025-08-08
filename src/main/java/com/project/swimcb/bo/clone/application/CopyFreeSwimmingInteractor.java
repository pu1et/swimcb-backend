package com.project.swimcb.bo.clone.application;

import com.project.swimcb.bo.clone.application.port.in.CopyFreeSwimmingUseCase;
import com.project.swimcb.bo.clone.application.port.out.CopyFreeSwimmingDsGateway;
import com.project.swimcb.bo.clone.domain.CopyFreeSwimmingCommand;
import com.project.swimcb.bo.freeswimming.application.port.in.CreateBoFreeSwimmingUseCase;
import com.project.swimcb.bo.freeswimming.domain.CreateBoFreeSwimmingCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CopyFreeSwimmingInteractor implements CopyFreeSwimmingUseCase {

  private final CopyFreeSwimmingDsGateway gateway;
  private final CreateBoFreeSwimmingUseCase createBoFreeSwimmingUseCase;

  @Override
  public void copyFreeSwimming(@NonNull CopyFreeSwimmingCommand command) {
    var candidateFreeSwimmings = gateway.findAllFreeSwimmingsByMonth(
        command.fromMonth());

    if (candidateFreeSwimmings.isEmpty()) {
      return;
    }

    candidateFreeSwimmings.stream()
        .map(i -> CreateBoFreeSwimmingCommand.builder()
            .swimmingPoolId(i.swimmingPoolId())
            .yearMonth(command.toMonth())
            .days(i.days().value())
            .time(
                CreateBoFreeSwimmingCommand.Time.builder()
                    .startTime(i.time().startTime())
                    .endTime(i.time().endTime())
                    .build()
            )
            .lifeguardId(i.lifeguardId())
            .tickets(i.tickets().stream()
                .map(ticket -> CreateBoFreeSwimmingCommand.Ticket.builder()
                    .name(ticket.name())
                    .price(ticket.price())
                    .build())
                .toList()
            )
            .capacity(i.capacity())
            .isExposed(true)
            .build())
        .forEach(createBoFreeSwimmingUseCase::createBoFreeSwimming);
  }

}
