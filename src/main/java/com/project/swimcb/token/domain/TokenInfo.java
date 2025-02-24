package com.project.swimcb.token.domain;

import com.project.swimcb.token.domain.enums.MemberRole;
import lombok.NonNull;

public record TokenInfo(
    long memberId,
    boolean isGuest,
    @NonNull MemberRole role
) {

}
