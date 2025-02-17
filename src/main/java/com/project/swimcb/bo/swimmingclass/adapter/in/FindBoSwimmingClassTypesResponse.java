package com.project.swimcb.bo.swimmingclass.adapter.in;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindBoSwimmingClassTypesResponse(
    @NonNull List<Type> types
) {

  @Builder
  record Type(
      long typeId,
      @NonNull String name,
      @NonNull List<SubType> subTypes
  ) {

  }

  @Builder
  record SubType(
      long subTypeId,
      @NonNull String name
  ) {

  }
}
