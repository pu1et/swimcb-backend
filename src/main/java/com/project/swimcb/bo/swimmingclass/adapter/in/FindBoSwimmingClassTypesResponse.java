package com.project.swimcb.bo.swimmingclass.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoSwimmingClassTypesResponse(
    @NonNull List<ClassType> classTypes
) {

  @Builder
  record ClassType(
      long classTypeId,
      @NonNull String name,
      @NonNull List<ClassSubType> classSubTypes
  ) {

  }

  @Builder
  record ClassSubType(
      long classSubTypeId,
      @NonNull String name
  ) {

  }
}
