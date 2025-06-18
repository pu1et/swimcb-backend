package com.project.swimcb.oauth2.application.port.out;

import com.project.swimcb.oauth2.domain.OAuth2Member;
import lombok.NonNull;

public interface OAuth2MemberGateway {

  OAuth2Member resolve(@NonNull String code);
}
