package com.project.swimcb.bo.swimmingpool.application;

import com.project.swimcb.bo.swimmingpool.application.in.UpdateSwimmingPoolBasicInfoUseCase;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolImage;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolImageRepository;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolRepository;
import com.project.swimcb.bo.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UpdateSwimmingPoolBasicInfoInteractor implements UpdateSwimmingPoolBasicInfoUseCase {

  private final SwimmingPoolRepository swimmingPoolRepository;
  private final SwimmingPoolImageRepository swimmingPoolImageRepository;

  @Override
  public void updateBasicInfo(long swimmingPoolId,
      @NonNull UpdateSwimmingPoolBasicInfoCommand request) {

    val swimmingPool = swimmingPoolRepository.findById(swimmingPoolId)
        .orElseThrow(() -> new IllegalArgumentException("수영장이 존재하지 않습니다."));

    swimmingPool.updateBasicInfo(request);

    updateSwimmingPoolImages(swimmingPool, request.imagePaths());
  }

  private void updateSwimmingPoolImages(SwimmingPool swimmingPool,
      @NonNull List<String> imagePaths) {

    swimmingPoolImageRepository.deleteAllBySwimmingPoolId(swimmingPool.getId());

    if (imagePaths.isEmpty()) {
      return;
    }

    val swimmingPoolImages = imagePaths.stream()
        .map(i -> SwimmingPoolImage.create(swimmingPool, i)).toList();

    swimmingPoolImageRepository.saveAll(swimmingPoolImages);
  }
}
