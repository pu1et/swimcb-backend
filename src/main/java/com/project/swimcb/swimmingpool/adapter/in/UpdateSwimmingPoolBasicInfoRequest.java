package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateSwimmingPoolBasicInfoRequest(

    @NotNull(message = "name은 null일 수 없습니다.")
    String name,

    @NotNull(message = "phone은 null일 수 없습니다.")
    String phone,

    @NotNull(message = "address은 null일 수 없습니다.")
    String address,

    @NotNull(message = "images은 null일 수 없습니다.")
    @Size(max = 6, message = "images는 6개 이하여야 합니다.")
    List<String> images,

    @NotNull(message = "usageAgreementUrl은 null일 수 없습니다.")
    String usageAgreementUrl
) {

  public UpdateSwimmingPoolBasicInfoCommand toCommand() {
    return UpdateSwimmingPoolBasicInfoCommand.builder()
        .name(this.name)
        .phone(this.phone)
        .address(this.address)
        .images(this.images)
        .usageAgreementUrl(this.usageAgreementUrl)
        .build();
  }

}
