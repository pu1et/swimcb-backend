package com.project.swimcb.bo.swimmingpool.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateSwimmingPoolBasicInfoCommand(
    String name,
    String phone,
    String address,
    Integer newRegistrationPeriodStartDay,
    Integer newRegistrationPeriodEndDay,
    Integer reRegistrationPeriodStartDay,
    Integer reRegistrationPeriodEndDay,
    String operatingDays,
    String closedDays,
    @NonNull List<String> imagePaths,
    String usageAgreementPath
) {

}
