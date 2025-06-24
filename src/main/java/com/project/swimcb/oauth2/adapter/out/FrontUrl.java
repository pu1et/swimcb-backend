package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.application.port.out.FrontUrlGateway;
import com.project.swimcb.oauth2.domain.enums.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class FrontUrl implements FrontUrlGateway {

  @Value("${url.front.local}")
  private String localUrl;

  @Value("${url.front.dev}")
  private String devUrl;

  @Override
  public String getUrl() {
    return devUrl;
  }

  @Override
  public String getUrl(Environment env) {
    if (env == null) {
      return devUrl;
    }
    return switch (env) {
      case LOCAL -> localUrl;
      case DEV -> devUrl;
    };
  }


}
