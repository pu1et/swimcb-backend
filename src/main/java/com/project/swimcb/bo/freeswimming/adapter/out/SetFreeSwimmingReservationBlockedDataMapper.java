package com.project.swimcb.bo.freeswimming.adapter.out;

import static com.project.swimcb.db.entity.QFreeSwimmingDayStatusEntity.freeSwimmingDayStatusEntity;
import static com.project.swimcb.db.entity.QFreeSwimmingEntity.freeSwimmingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;

import com.project.swimcb.bo.freeswimming.application.port.out.SetFreeSwimmingReservationBlockedDsGateway;
import com.project.swimcb.bo.freeswimming.domain.SetFreeSwimmingReservationBlockedCommand;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class SetFreeSwimmingReservationBlockedDataMapper implements
    SetFreeSwimmingReservationBlockedDsGateway {

  private final JPAQueryFactory queryFactory;
  private final EntityManager entityManager;

  @Override
  public boolean existsAllByIdInAndSwimmingPoolId(
      @NonNull List<Long> freeSwimmingDayStatusIds,
      @NonNull Long swimmingPoolId
  ) {

    val result = queryFactory.select(freeSwimmingDayStatusEntity.id)
        .from(freeSwimmingDayStatusEntity)
        .join(freeSwimmingDayStatusEntity.freeSwimming, freeSwimmingEntity)
        .join(freeSwimmingEntity.swimmingPool, swimmingPoolEntity)
        .where(
            freeSwimmingDayStatusEntity.id.in(freeSwimmingDayStatusIds),
            swimmingPoolEntity.id.ne(swimmingPoolId)
        )
        .fetch();

    return result.isEmpty();
  }

  @Override
  public void setFreeSwimmingReservationBlocked(@NonNull SetFreeSwimmingReservationBlockedCommand command) {
    val now = LocalDateTime.now();

    queryFactory.update(freeSwimmingDayStatusEntity)
        .set(freeSwimmingDayStatusEntity.isReservationBlocked, command.isReservationBlocked())
        .set(freeSwimmingDayStatusEntity.updatedAt, now)
        .where(
            freeSwimmingDayStatusEntity.id.in(command.freeSwimmingDayStatusIds())
        )
        .execute();

    entityManager.flush();
    entityManager.clear();
  }

}
