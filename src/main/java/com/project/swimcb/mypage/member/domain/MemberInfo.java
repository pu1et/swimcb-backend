package com.project.swimcb.mypage.member.domain;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record MemberInfo(
    String name,
    String phone,
    @NonNull String email
) {

}
