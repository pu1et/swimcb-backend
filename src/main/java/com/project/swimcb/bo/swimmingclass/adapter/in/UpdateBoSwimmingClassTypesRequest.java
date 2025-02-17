package com.project.swimcb.bo.swimmingclass.adapter.in;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateBoSwimmingClassTypesRequest(
    @Valid
    @NotNull
    @Size(min = 6, max = 6, message = "강습형태는 6개로 고정되어 있습니다.")
    List<Type> types
) {

  @Builder
  record Type(
      long typeId,

      @NotNull(message = "강습형태 이름은 null이 될 수 없습니다.")
      String name,

      @Valid
      @NotNull(message = "강습구분 리스트는 null이 될 수 없습니다.")
      @Size(max = 6, message = "강습구분 리스트는 6개 이하로만 설정할 수 있습니다.")
      List<SubType> subTypes
  ) {

  }

  @Builder
  record SubType(
      Long subTypeId,

      @NotNull(message = "강습구분 이름은 null이 될 수 없습니다.")
      String name
  ) {

  }
}
