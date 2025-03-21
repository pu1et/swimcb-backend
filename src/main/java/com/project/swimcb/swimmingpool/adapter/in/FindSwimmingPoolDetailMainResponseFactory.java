package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
import lombok.NonNull;

public interface FindSwimmingPoolDetailMainResponseFactory {

  FindSwimmingPoolDetailMainResponse create(
      @NonNull SwimmingPoolDetailMain swimmingPoolDetailMain);
}
