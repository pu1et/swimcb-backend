package com.project.swimcb.bo.swimmingpool.application;

import com.project.swimcb.bo.swimmingpool.application.in.UpdateSwimmingPoolBasicInfoUseCase;
import com.project.swimcb.bo.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateSwimmingPoolBasicInfoInteractor implements UpdateSwimmingPoolBasicInfoUseCase {

  @Override
  public void updateBasicInfo(long swimmingPoolId,
      @NonNull UpdateSwimmingPoolBasicInfoCommand request) {

  }
}
