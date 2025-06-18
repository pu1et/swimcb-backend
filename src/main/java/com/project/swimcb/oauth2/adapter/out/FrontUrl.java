package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.application.port.out.FrontUrlGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class FrontUrl implements FrontUrlGateway {

  @Value("${url.front}")
  private String frontUrl;

  @Override
  public String getUrl() {
    return frontUrl;
  }

}
