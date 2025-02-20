package com.project.swimcb.bo.swimmingpool.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateSwimmingPoolBasicInfoCommand(
    String name,
    String phone,
    String address,
    Integer newEnrollmentStartDate,
    Integer newEnrollmentEndDate,
    Integer reEnrollmentStartDate,
    Integer reEnrollmentEndDate,
    @NonNull List<String> imagePaths,
    String usageAgreementPath
) {

}
