package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.swimmingClass;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassTicket.swimmingClassTicket;

import com.project.swimcb.bo.instructor.domain.SwimmingInstructor;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
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

    val count = queryFactory.update(swimmingClass)
        .set(swimmingClass.type, classType)
        .set(swimmingClass.subType, classSubType)
        .set(swimmingClass.daysOfWeek, days)
        .set(swimmingClass.startTime, request.time().startTime())
        .set(swimmingClass.endTime, request.time().endTime())
        .set(swimmingClass.instructor, instructor)
        .set(swimmingClass.totalCapacity, request.registrationCapacity().totalCapacity())
        .set(swimmingClass.reservationLimitCount,
            request.registrationCapacity().reservationLimitCount())
        .set(swimmingClass.isVisible, request.isExposed())
        .where(
            swimmingClass.swimmingPool.id.eq(request.swimmingPoolId()),
            swimmingClass.id.eq(request.swimmingClassId())
        )
        .execute();

    if (count != 1) {
      throw new NoSuchElementException("클래스가 존재하지 않습니다.");
    }
  }

  @Override
  public void deleteAllTicketsBySwimmingClassId(@NonNull Long swimmingClassId) {
    queryFactory.update(swimmingClassTicket)
        .set(swimmingClassTicket.isDeleted, true)
        .where(swimmingClassTicket.swimmingClass.id.eq(swimmingClassId))
        .execute();

    entityManager.flush();
    entityManager.clear();
  }

  private SwimmingClassType findClassType(long classTypeId) {
    return swimmingClassTypeRepository.findById(classTypeId)
        .orElseThrow(() -> new NoSuchElementException("강습형태가 존재하지 않습니다."));
  }

  private SwimmingClassSubType findClassSubType(long classSubTypeId) {
    return swimmingClassSubTypeRepository.findById(classSubTypeId)
        .orElseThrow(() -> new NoSuchElementException("강습구분이 존재하지 않습니다."));
  }

  private SwimmingInstructor findInstructor(long instructorId) {
    return swimmingInstructorRepository.findById(instructorId)
        .orElseThrow(() -> new NoSuchElementException("강사가 존재하지 않습니다."));
  }

  private int days(@NonNull List<DayOfWeek> days) {
    return days.stream().map(i -> 1 << (6 - (i.getValue() - 1))).reduce(0, Integer::sum);
  }
}
