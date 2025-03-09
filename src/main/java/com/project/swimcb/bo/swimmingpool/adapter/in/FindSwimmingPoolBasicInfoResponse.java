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
    Integer newRegistrationPeriodStartDay,

    @Schema(description = "신규모집기간 종료일", example = "10")
    Integer newRegistrationPeriodEndDay,

    @Schema(description = "재등록기간 시작일", example = "15")
    Integer reRegistrationPeriodStartDay,

    @Schema(description = "재등록기간 종료일", example = "20")
    Integer reRegistrationPeriodEndDay,

    @Schema(description = "신규모집기간 이후 신청 가능 여부", example = "true")
    boolean isNewRegistrationExtended,

    @Schema(description = "운영일", example = "월, 화, 수, 목, 금")
    String operatingDays,

    @Schema(description = "휴무일", example = "격주 토요일")
    String closedDays,

    @Schema(description = "대표 이미지 URL 목록")
    @NonNull List<String> representativeImageUrls,

    @Schema(description = "이용약관 URL", example = "http://host.com/usage-agreement.txt")
    String usageAgreementUrl
) {

}
