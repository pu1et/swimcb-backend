package com.project.swimcb.swimmingpool.application.in;

import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFreeSwimmingDetailCondition;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFreeSwimmingDetail;
import lombok.NonNull;

public interface FindSwimmingPoolDetailFreeSwimmingDetailUseCase {

  SwimmingPoolDetailFreeSwimmingDetail findSwimmingPoolDetailFreeSwimmingDetail(
      @NonNull FindSwimmingPoolDetailFreeSwimmingDetailCondition condition);
}
