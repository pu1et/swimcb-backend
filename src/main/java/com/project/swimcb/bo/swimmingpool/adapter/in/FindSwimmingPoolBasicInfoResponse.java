package com.project.swimcb.bo.swimmingpool.adapter.in;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
@Schema(description = "BO 수영장 기본 정보 조회 response")
public record FindSwimmingPoolBasicInfoResponse(

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

    @Schema(description = "대표 이미지 URL 목록")
    @NonNull List<String> imageUrls,

    @Schema(description = "이용약관 URL", example = "/swimming-pool/image/1")
    String usageAgreementUrl
) {

}
