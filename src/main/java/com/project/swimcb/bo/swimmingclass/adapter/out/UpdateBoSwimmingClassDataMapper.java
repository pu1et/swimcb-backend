package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.SWIMMING_CLASS;

import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import com.project.swimcb.db.entity.SwimmingClassSubTypeEntity;
import com.project.swimcb.db.entity.SwimmingClassTypeEntity;
import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import com.project.swimcb.db.repository.SwimmingClassSubTypeRepository;
import com.project.swimcb.db.repository.SwimmingClassTypeRepository;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.DayOfWeek;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateBoSwimmingClassDataMapper implements UpdateBoSwimmingClassDsGateway {

  private final JPAQueryFactory queryFactory;
  private final SwimmingClassTypeRepository swimmingClassTypeRepository;
  private final SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;
  private final SwimmingInstructorRepository swimmingInstructorRepository;
  private final EntityManager entityManager;

  @Override
  public void updateSwimmingClass(@NonNull UpdateBoSwimmingClassCommand request) {
    val classType = findClassType(request.type().typeId());
    val classSubType = findClassSubType(request.type().subTypeId());
    val instructor = findInstructor(request.instructorId());
    val days = days(request.days());

    val count = queryFactory.update(swimmingClassEntity)
        .set(swimmingClassEntity.type, classType)
        .set(swimmingClassEntity.subType, classSubType)
        .set(swimmingClassEntity.daysOfWeek, days)
        .set(swimmingClassEntity.startTime, request.time().startTime())
        .set(swimmingClassEntity.endTime, request.time().endTime())
        .set(swimmingClassEntity.instructor, instructor)
        .set(swimmingClassEntity.totalCapacity, request.registrationCapacity().totalCapacity())
        .set(swimmingClassEntity.reservationLimitCount,
            request.registrationCapacity().reservationLimitCount())
        .set(swimmingClassEntity.isVisible, request.isExposed())
        .where(
            swimmingClassEntity.swimmingPool.id.eq(request.swimmingPoolId()),
            swimmingClassEntity.id.eq(request.swimmingClassId())
        )
        .execute();

    if (count != 1) {
      throw new NoSuchElementException("클래스가 존재하지 않습니다.");
    }
  }

  @Override
  public void deleteAllTicketsBySwimmingClassId(@NonNull Long swimmingClassId) {
    queryFactory.update(ticketEntity)
        .set(ticketEntity.isDeleted, true)
        .where(
            ticketEntity.targetId.eq(swimmingClassId),
            ticketEntity.targetType.eq(SWIMMING_CLASS)
        )
        .execute();

    entityManager.flush();
    entityManager.clear();
  }

  private SwimmingClassTypeEntity findClassType(long classTypeId) {
    return swimmingClassTypeRepository.findById(classTypeId)
        .orElseThrow(() -> new NoSuchElementException("강습형태가 존재하지 않습니다."));
  }

  private SwimmingClassSubTypeEntity findClassSubType(long classSubTypeId) {
    return swimmingClassSubTypeRepository.findById(classSubTypeId)
        .orElseThrow(() -> new NoSuchElementException("강습구분이 존재하지 않습니다."));
  }

  private SwimmingInstructorEntity findInstructor(Long instructorId) {
    if (instructorId == null) {
      return null;
    }
    return swimmingInstructorRepository.findById(instructorId)
        .orElseThrow(() -> new NoSuchElementException("강사가 존재하지 않습니다."));
  }

  private int days(@NonNull List<DayOfWeek> days) {
    return days.stream().map(i -> 1 << (6 - (i.getValue() - 1))).reduce(0, Integer::sum);
  }

}
