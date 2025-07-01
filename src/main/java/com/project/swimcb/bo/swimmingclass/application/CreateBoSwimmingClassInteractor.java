package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import com.project.swimcb.db.entity.TicketEntity;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.db.repository.SwimmingClassSubTypeRepository;
import com.project.swimcb.db.repository.SwimmingClassTicketRepository;
import com.project.swimcb.db.repository.SwimmingClassTypeRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import jakarta.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
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

    val instructor = findInstructor(command.instructorId());

    val swimmingClass = SwimmingClassEntity.builder()
        .swimmingPool(pool)
        .year(LocalDate.now().getYear())
        .month(command.month())
        .type(classType)
        .subType(classSubType)
        .daysOfWeek(daysOfWeek(command.days()))
        .startTime(command.time().startTime())
        .endTime(command.time().endTime())
        .instructor(instructor)
        .totalCapacity(command.registrationCapacity().totalCapacity())
        .reservationLimitCount(command.registrationCapacity().reservationLimitCount())
        .isVisible(command.isExposed())
        .isCanceled(false)
        .build();

    val savedSwimmingClass = swimmingClassRepository.save(swimmingClass);

    val tickets = command.tickets().stream()
        .map(i -> TicketEntity.create(savedSwimmingClass, i.name(), i.price())).toList();

    swimmingClassTicketRepository.saveAll(tickets);
  }

  private SwimmingInstructorEntity findInstructor(Long instructorId) {
    if (instructorId == null) {
      return null;
    }

    return instructorRepository.findById(instructorId)
        .orElseThrow(() -> new IllegalArgumentException("강사가 존재하지 않습니다."));
  }

  private int daysOfWeek(@NonNull List<DayOfWeek> days) {
    return days.stream()
        .map(i -> 1 << (6 - (i.getValue() - 1)))
        .reduce(0, (a, b) -> a | b);
  }
}
