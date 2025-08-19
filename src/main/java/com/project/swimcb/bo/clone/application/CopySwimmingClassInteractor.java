package com.project.swimcb.bo.clone.application;

import static com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand.RegistrationCapacity;
import static com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand.Ticket;
import static com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand.Type;

import com.project.swimcb.bo.clone.application.port.in.CopySwimmingClassUseCase;
import com.project.swimcb.bo.clone.application.port.out.CopySwimmingClassDsGateway;
import com.project.swimcb.bo.clone.domain.CopySwimmingClassCommand;
import com.project.swimcb.bo.clone.domain.SwimmingClassCopyCandidate;
import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand.Time;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CopySwimmingClassInteractor implements CopySwimmingClassUseCase {

  private final CopySwimmingClassDsGateway gateway;
  private final CreateBoSwimmingClassUseCase createBoFreeSwimmingUseCase;

  @Override
  public void copySwimmingClass(@NonNull CopySwimmingClassCommand command) {
    val candidateSwimmingClasses = gateway.findAllSwimmingClassesByMonth(
        command.fromMonth());

    if (candidateSwimmingClasses.isEmpty()) {
      return;
    }

    gateway.deleteSwimmingClassByMonth(command.toMonth());

    buildCreateBoSwimmingClassCommands(command, candidateSwimmingClasses)
        .forEach(createBoFreeSwimmingUseCase::createBoSwimmingClass);
  }

  private Stream<CreateBoSwimmingClassCommand> buildCreateBoSwimmingClassCommands(
      @NonNull CopySwimmingClassCommand command,
      @NonNull List<SwimmingClassCopyCandidate> candidateSwimmingClasses
  ) {
    return candidateSwimmingClasses
        .stream()
        .map(i -> CreateBoSwimmingClassCommand.builder()
            .swimmingPoolId(i.swimmingPoolId())
            .month(command.toMonth().getMonthValue())
            .days(i.dayOfWeek().value())
            .time(
                Time.builder()
                    .startTime(i.time().startTime())
                    .endTime(i.time().endTime())
                    .build()
            )
            .type(
                Type.builder()
                    .classTypeId(i.type().classTypeId())
                    .classSubTypeId(i.type().classSubTypeId())
                    .build()
            )
            .instructorId(i.instructorId())
            .tickets(
                i.tickets().stream()
                    .map(ticket -> Ticket.builder()
                        .name(ticket.name())
                        .price(ticket.price())
                        .build())
                    .toList()
            )
            .registrationCapacity(
                RegistrationCapacity.builder()
                    .totalCapacity(i.registrationCapacity().totalCapacity())
                    .reservationLimitCount(i.registrationCapacity().reservationLimitCount())
                    .build()
            )
            .isExposed(true)
            .build());
  }

}
