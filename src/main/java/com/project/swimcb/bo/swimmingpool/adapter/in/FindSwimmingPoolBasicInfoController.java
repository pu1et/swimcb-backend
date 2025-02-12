package com.project.swimcb.bo.swimmingpool.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/swimming-pools/{swimmingPoolId}/basic-info")
@RequiredArgsConstructor
public class FindSwimmingPoolBasicInfoController {

  @Operation(summary = "수영장 기본 정보 조회")
  @GetMapping
  public FindSwimmingPoolBasicInfoResponse findBasicInfo(
      @PathVariable("swimmingPoolId") long swimmingPoolId) {

    return FindSwimmingPoolBasicInfoResponse.builder()
        .name("올림픽수영장")
        .phone("02-1234-5678")
        .address("서울시 강남구 언주로")
        .newEnrollmentStartDay(1)
        .newEnrollmentEndDay(10)
        .reEnrollmentStartDate(15)
        .reEnrollmentEndDate(20)
        .imageUrls(List.of("/swimming-pool/image1.jpg", "/swimming-pool/image2.jpg"))
        .usageAgreementUrl("/swimming-pool/usage-agreement/usage-agreement.txt")
        .build();
  }
}
