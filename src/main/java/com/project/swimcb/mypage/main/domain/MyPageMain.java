package com.project.swimcb.mypage.main.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record MyPageMain(
    String profileImageUrl,
    @NonNull String name,
    @NonNull String phoneNumber,
    int reservationCount
) {

}
