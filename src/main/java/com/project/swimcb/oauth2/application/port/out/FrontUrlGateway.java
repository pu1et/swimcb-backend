package com.project.swimcb.oauth2.application.port.out;

import com.project.swimcb.oauth2.domain.enums.Environment;

public interface FrontUrlGateway {

  String getUrl();

  String getUrl(Environment env);
}
