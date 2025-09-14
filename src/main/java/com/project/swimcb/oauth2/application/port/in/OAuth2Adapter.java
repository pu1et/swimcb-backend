package com.project.swimcb.oauth2.application.port.in;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.domain.OAuth2Request;
import lombok.NonNull;

public interface OAuth2Adapter<T extends OAuth2Request> {

  OAuth2Response success(@NonNull T request);

}
