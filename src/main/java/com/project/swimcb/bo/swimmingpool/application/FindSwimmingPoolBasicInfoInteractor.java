package com.project.swimcb.bo.swimmingpool.application;

import com.project.swimcb.bo.swimmingpool.adapter.in.FindSwimmingPoolBasicInfoResponse;
import com.project.swimcb.bo.swimmingpool.application.in.FindSwimmingPoolBasicInfoUseCase;
import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolImageRepository;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindSwimmingPoolBasicInfoInteractor implements FindSwimmingPoolBasicInfoUseCase {

  private final SwimmingPoolRepository swimmingPoolRepository;
  private final SwimmingPoolImageRepository swimmingPoolImageRepository;
  private final ImageUrlPort imageUrlPort;

  @Override
  public FindSwimmingPoolBasicInfoResponse findSwimmingPoolBasicInfo(long swimmingPoolId) {
    val swimmingPool = swimmingPoolRepository.findById(swimmingPoolId)
        .orElseThrow(() -> new IllegalArgumentException("수영장을 찾을 수 없습니다. " + swimmingPoolId));

    val swimmingPoolImageUrls = swimmingPoolImageRepository.findBySwimmingPoolId(swimmingPoolId)
        .stream().map(i -> getImageUrl(i.getPath())).toList();

    return FindSwimmingPoolBasicInfoResponse.builder()
        .name(swimmingPool.getName())
        .phone(swimmingPool.getPhone())
        .address(swimmingPool.getAddress())
        .newRegistrationPeriodStartDay(swimmingPool.getNewRegistrationPeriodStartDay())
        .newRegistrationPeriodEndDay(swimmingPool.getNewRegistrationPeriodEndDay())
        .reRegistrationPeriodStartDay(swimmingPool.getReRegistrationPeriodStartDay())
        .reRegistrationPeriodEndDay(swimmingPool.getReRegistrationPeriodEndDay())
        .isNewRegistrationExtended(swimmingPool.isNewRegistrationExtended())
        .operatingDays(swimmingPool.getOperatingDays())
        .closedDays(swimmingPool.getClosedDays())
        .representativeImageUrls(swimmingPoolImageUrls)
        .usageAgreementUrl(getImageUrl(swimmingPool.getUsageAgreementPath()))
        .build();
  }

  private String getImageUrl(String imagePath) {
    return imageUrlPort.getImageUrl(imagePath);
  }
}
