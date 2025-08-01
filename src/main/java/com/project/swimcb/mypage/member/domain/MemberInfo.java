package com.project.swimcb.mypage.member.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record MemberInfo(
    @NonNull String name,
    @NonNull String phone,
    @NonNull String email
) {

}
