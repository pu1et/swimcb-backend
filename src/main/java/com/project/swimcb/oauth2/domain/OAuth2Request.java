package com.project.swimcb.oauth2.domain;

import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;

public interface OAuth2Request {

  OAuth2ProviderType getProvider();

}
