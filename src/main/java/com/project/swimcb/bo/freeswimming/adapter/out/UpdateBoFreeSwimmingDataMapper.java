package com.project.swimcb.bo.freeswimming.adapter.out;

import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QTicketEntity.ticketEntity;
import static com.project.swimcb.db.entity.TicketTargetType.FREE_SWIMMING;

import com.project.swimcb.bo.freeswimming.adapter.in.UpdateBoFreeSwimmingCommand;
import com.project.swimcb.bo.freeswimming.application.port.out.UpdateBoFreeSwimmingDsGateway;
import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UpdateBoFreeSwimmingDataMapper implements UpdateBoFreeSwimmingDsGateway {

  private final JPAQueryFactory queryFactory;
  private final EntityManager entityManager;

  @Override
  public void updateFreeSwimming(@NonNull UpdateBoFreeSwimmingCommand command) {

    val lifeguard = toSwimmingInstructorEntity(command.lifeguardId());
    val now = LocalDateTime.now();

    queryFactory.update(freeSwimmingEntity)
        .set(freeSwimmingEntity.daysOfWeek, command.daysOfWeek().toInt())
        .set(freeSwimmingEntity.startTime, command.time().startTime())
        .set(freeSwimmingEntity.endTime, command.time().endTime())
        .set(freeSwimmingEntity.lifeguard, lifeguard)
        .set(freeSwimmingEntity.capacity, command.capacity())
        .set(freeSwimmingEntity.isVisible, command.isExposed())
        .set(freeSwimmingEntity.updatedAt, now)
        .where(
            freeSwimmingEntity.swimmingPool.id.eq(command.swimmingPoolId()),
            freeSwimmingEntity.id.eq(command.freeSwimmingId())
        )
        .execute();

    entityManager.flush();
    entityManager.clear();
  }

  @Override
  public void deleteAllTicketsByFreeSwimmingId(@NonNull Long freeSwimmingId) {
    val now = LocalDateTime.now();

    queryFactory.update(ticketEntity)
        .set(ticketEntity.isDeleted, true)
        .set(ticketEntity.updatedAt, now)
        .where(
            ticketEntity.targetId.eq(freeSwimmingId),
            ticketEntity.targetType.eq(FREE_SWIMMING)
        )
        .execute();

    entityManager.flush();
    entityManager.clear();
  }

  private SwimmingInstructorEntity toSwimmingInstructorEntity(Long lifeguardId) {
    if (lifeguardId == null) {
      return null;
    }
    return new SwimmingInstructorEntity(lifeguardId);
  }

}
