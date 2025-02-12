package com.project.swimcb.bo.swimmingpool.adapter.in;

import com.project.swimcb.bo.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
@Schema(description = "BO 수영장 기본 정보 수정 request")
public record UpdateSwimmingPoolBasicInfoRequest(

    @Schema(description = "수영장 이름", example = "올림픽수영장")
    String name,

    @Schema(description = "전화번호", example = "02-1234-5678")
    String phone,

    @Schema(description = "주소", example = "서울시 강남구 언주로")
    String address,

    @Schema(description = "신규모집기간 시작일", example = "1")
    Integer newEnrollmentStartDay,

    @Schema(description = "신규모집기간 종료일", example = "10")
    Integer newEnrollmentEndDay,

    @Schema(description = "재등록기간 시작일", example = "15")
    Integer reEnrollmentStartDate,

    @Schema(description = "재등록기간 종료일", example = "20")
    Integer reEnrollmentEndDate,

    @NotNull(message = "images은 null일 수 없습니다.")
    @Size(max = 6, message = "images는 6개 이하여야 합니다.")
    @Schema(description = "대표 이미지")
    List<String> images,

    @Schema(description = "이용약관 Path", example = "/swimming-pool/image/1")
    String usageAgreementPath
) {

  public UpdateSwimmingPoolBasicInfoCommand toCommand() {
    return UpdateSwimmingPoolBasicInfoCommand.builder()
        .name(this.name)
        .phone(this.phone)
        .address(this.address)
        .newEnrollmentStartDate(this.newEnrollmentStartDay)
        .newEnrollmentEndDate(this.newEnrollmentEndDay)
        .reEnrollmentStartDate(this.reEnrollmentStartDate)
        .reEnrollmentEndDate(this.reEnrollmentEndDate)
        .images(this.images)
        .usageAgreementUrl(this.usageAgreementPath)
        .build();
  }
}
