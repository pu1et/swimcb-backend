package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClass.*;

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
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class UpdateBoSwimmingClassDataMapper implements UpdateBoSwimmingClassDsGateway {

  private final JPAQueryFactory queryFactory;
  private final SwimmingClassTypeRepository swimmingClassTypeRepository;
  private final SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;
  private final SwimmingInstructorRepository swimmingInstructorRepository;

  public UpdateBoSwimmingClassDataMapper(EntityManager entityManager,
      SwimmingClassTypeRepository swimmingClassTypeRepository,
      SwimmingClassSubTypeRepository swimmingClassSubTypeRepository,
      SwimmingInstructorRepository swimmingInstructorRepository) {
    this.queryFactory = new JPAQueryFactory(entityManager);
    this.swimmingClassTypeRepository = swimmingClassTypeRepository;
    this.swimmingClassSubTypeRepository = swimmingClassSubTypeRepository;
    this.swimmingInstructorRepository = swimmingInstructorRepository;
  }

  @Override
  public void updateSwimmingClass(@NonNull UpdateBoSwimmingClassCommand request) {
    val classType = findClassType(request.type().typeId());
    val classSubType = findClassSubType(request.type().subTypeId());
    val instructor = findInstructor(request.instructorId());

    queryFactory.update(swimmingClass)
        .set(swimmingClass.type, classType)
        .set(swimmingClass.subType, classSubType)
        .set(swimmingClass.isMonday, request.days().isMonday())
        .set(swimmingClass.isTuesday, request.days().isTuesday())
        .set(swimmingClass.isWednesday, request.days().isWednesday())
        .set(swimmingClass.isThursday, request.days().isThursday())
        .set(swimmingClass.isFriday, request.days().isFriday())
        .set(swimmingClass.isSaturday, request.days().isSaturday())
        .set(swimmingClass.isSunday, request.days().isSunday())
        .set(swimmingClass.startTime, request.time().startTime())
        .set(swimmingClass.endTime, request.time().endTime())
        .set(swimmingClass.instructor, instructor)
        .set(swimmingClass.totalCapacity, request.registrationCapacity().totalCapacity())
        .set(swimmingClass.reservationLimitCount, request.registrationCapacity().reservationLimitCount())
        .set(swimmingClass.isVisible, request.isExposed())
        .where(
            swimmingClass.swimmingPool.id.eq(request.swimmingPoolId()),
            swimmingClass.id.eq(request.swimmingClassId())
        )
        .execute();
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
}
