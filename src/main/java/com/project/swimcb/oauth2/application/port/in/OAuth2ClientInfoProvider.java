package com.project.swimcb.oauth2.application.port.in;

import com.project.swimcb.oauth2.domain.OAuth2ClientInfo;

public interface OAuth2ClientInfoProvider {

  OAuth2ClientInfo getOAuth2ClientInfo();

}
