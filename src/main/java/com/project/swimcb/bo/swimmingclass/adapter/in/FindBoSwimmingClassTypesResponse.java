package com.project.swimcb.bo.swimmingclass.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
public record FindBoSwimmingClassTypesResponse(
    @NonNull List<ClassType> classTypes
) {

  @Builder
  public record ClassType(
      long classTypeId,
      @NonNull String name,
      @NonNull List<ClassSubType> classSubTypes
  ) {

  }

  @Builder
  public record ClassSubType(
      long classSubTypeId,
      @NonNull String name
  ) {

  }
}
