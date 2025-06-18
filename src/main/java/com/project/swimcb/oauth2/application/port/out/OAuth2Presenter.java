package com.project.swimcb.oauth2.application.port.out;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import lombok.NonNull;

public interface OAuth2Presenter {

  OAuth2Response signup(@NonNull String accessToken);

  OAuth2Response login(@NonNull String accessToken);
}
