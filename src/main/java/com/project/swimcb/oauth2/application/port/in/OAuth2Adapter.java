package com.project.swimcb.oauth2.application.port.in;

import com.project.swimcb.oauth2.adapter.in.OAuth2Request;
import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import lombok.NonNull;

public interface OAuth2Adapter {

  OAuth2Response success(@NonNull OAuth2Request request);
}
