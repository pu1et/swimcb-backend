package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateSwimmingPoolBasicInfoRequest(
    String name,
    String phone,
    String address,
    Integer newEnrollmentStartDate,
    Integer newEnrollmentEndDate,
    Integer reEnrollmentStartDate,
    Integer reEnrollmentEndDate,
    @NotNull(message = "images은 null일 수 없습니다.")
    @Size(max = 6, message = "images는 6개 이하여야 합니다.")
    List<String> images,
    String usageAgreementUrl
) {

  public UpdateSwimmingPoolBasicInfoCommand toCommand() {
    return UpdateSwimmingPoolBasicInfoCommand.builder()
        .name(this.name)
        .phone(this.phone)
        .address(this.address)
        .newEnrollmentStartDate(this.newEnrollmentStartDate)
        .newEnrollmentEndDate(this.newEnrollmentEndDate)
        .reEnrollmentStartDate(this.reEnrollmentStartDate)
        .reEnrollmentEndDate(this.reEnrollmentEndDate)
        .images(this.images)
        .usageAgreementUrl(this.usageAgreementUrl)
        .build();
  }

}
