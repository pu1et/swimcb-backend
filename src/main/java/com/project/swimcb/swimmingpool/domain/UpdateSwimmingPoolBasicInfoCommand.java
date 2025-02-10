package com.project.swimcb.swimmingpool.domain;

import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record UpdateSwimmingPoolBasicInfoCommand(
    @NonNull String name,
    @NonNull String phone,
    @NonNull String address,
    @NonNull List<String> images,
    @NonNull String usageAgreementUrl
) {

}
