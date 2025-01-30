package com.project.swimcb.mypage.member.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record MemberInfo(
    @NonNull String name,
    @NonNull String phoneNumber,
    @NonNull String email
) {

}
