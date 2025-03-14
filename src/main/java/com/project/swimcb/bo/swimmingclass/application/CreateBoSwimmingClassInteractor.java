package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicket;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicketRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTypeRepository;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CreateBoSwimmingClassInteractor implements CreateBoSwimmingClassUseCase {

  private final SwimmingPoolRepository swimmingPoolRepository;
  private final SwimmingClassRepository swimmingClassRepository;
  private final SwimmingClassTypeRepository swimmingClassTypeRepository;
  private final SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;
  private final SwimmingInstructorRepository instructorRepository;
  private final SwimmingClassTicketRepository swimmingClassTicketRepository;

  @Override
  public void createBoSwimmingClass(@NonNull CreateBoSwimmingClassCommand command) {
    val pool = swimmingPoolRepository.findById(command.swimmingPoolId())
        .orElseThrow(() -> new IllegalArgumentException("수영장이 존재하지 않습니다."));

    val classType = swimmingClassTypeRepository.findById(command.type().classTypeId())
        .orElseThrow(() -> new IllegalArgumentException("강습형태가 존재하지 않습니다."));

    val classSubType = swimmingClassSubTypeRepository.findById(command.type().classSubTypeId())
        .orElseThrow(() -> new IllegalArgumentException("강습구분이 존재하지 않습니다."));

    val instructor = instructorRepository.findById(command.instructorId())
        .orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));

    val swimmingClass = SwimmingClass.builder()
        .swimmingPool(pool)
        .month(command.month())
        .type(classType)
        .subType(classSubType)
        .isMonday(command.days().isMonday())
        .isTuesday(command.days().isTuesday())
        .isWednesday(command.days().isWednesday())
        .isThursday(command.days().isThursday())
        .isFriday(command.days().isFriday())
        .isSaturday(command.days().isSaturday())
        .isSunday(command.days().isSunday())
        .startTime(command.time().startTime())
        .endTime(command.time().endTime())
        .instructor(instructor)
        .totalCapacity(command.registrationCapacity().totalCapacity())
        .reservationLimitCount(command.registrationCapacity().reservationLimitCount())
        .isVisible(command.isExposed())
        .build();

    val savedSwimmingClass = swimmingClassRepository.save(swimmingClass);

    val tickets = command.tickets().stream()
        .map(i -> SwimmingClassTicket.create(savedSwimmingClass, i.name(), i.price())).toList();

    swimmingClassTicketRepository.saveAll(tickets);
  }
}
