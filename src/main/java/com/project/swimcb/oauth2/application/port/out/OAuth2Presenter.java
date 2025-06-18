package com.project.swimcb.oauth2.application.port.out;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;

public interface OAuth2Presenter {

  OAuth2Response signup();

  OAuth2Response login();
}
