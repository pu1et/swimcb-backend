package com.project.swimcb.mypage.member.adapter.in;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record FindMemberInfoResponse(
    @NonNull String name,
    @NonNull String phone,
    @NonNull String email
) {

}
