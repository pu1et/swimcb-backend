package com.project.swimcb.oauth2.application;

import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.adapter.in.OAuth2Request;
import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
class OAuth2Interactor implements OAuth2Adapter {

  @Override
  public OAuth2Response success(@NonNull OAuth2Request request) {
    return null;
  }

}
