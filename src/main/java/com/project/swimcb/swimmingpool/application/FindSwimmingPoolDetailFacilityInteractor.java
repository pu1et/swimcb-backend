package com.project.swimcb.swimmingpool.application;

import com.project.swimcb.db.repository.SwimmingPoolRepository;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFacilityUseCase;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFacility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
class FindSwimmingPoolDetailFacilityInteractor implements
    FindSwimmingPoolDetailFacilityUseCase {

  private final SwimmingPoolRepository swimmingPoolRepository;

  @Override
  public SwimmingPoolDetailFacility findSwimmingPoolDetailFacility(@NonNull Long swimmingPoolId) {
    val swimmingPool = swimmingPoolRepository.findById(swimmingPoolId)
        .orElseThrow(
            () -> new IllegalArgumentException("해당 수영장 정보가 존재하지 않습니다 : " + swimmingPoolId));

    return SwimmingPoolDetailFacility.builder()
        .operatingDays(swimmingPool.getOperatingDays())
        .closedDays(swimmingPool.getClosedDays())
        .newRegistrationPeriodStartDay(swimmingPool.getNewRegistrationPeriodStartDay())
        .newRegistrationPeriodEndDay(swimmingPool.getNewRegistrationPeriodEndDay())
        .reRegistrationPeriodStartDay(swimmingPool.getReRegistrationPeriodStartDay())
        .reRegistrationPeriodEndDay(swimmingPool.getReRegistrationPeriodEndDay())
        .build();
  }

}
