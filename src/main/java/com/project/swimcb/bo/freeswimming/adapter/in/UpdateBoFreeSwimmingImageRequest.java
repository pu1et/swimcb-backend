package com.project.swimcb.bo.freeswimming.adapter.in;

import jakarta.validation.constraints.NotNull;

public record UpdateBoFreeSwimmingImageRequest(
    @NotNull(message = "imagePath는 null일 수 없습니다.")
    String imagePath
) {

}
