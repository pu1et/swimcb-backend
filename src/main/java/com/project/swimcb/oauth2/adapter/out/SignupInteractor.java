package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.SignupRequest;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
class SignupInteractor implements SignupPort {

  @Override
  public boolean signup(@NonNull SignupRequest signupRequest) {
    return false;
  }

}
