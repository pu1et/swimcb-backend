package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.out.FrontUrlGateway;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
class OAuth2Formatter implements OAuth2Presenter {

  private final FrontUrlGateway frontUrlGateway;

  @Override
  public OAuth2Response signup(@NonNull String accessToken) {
    val frontUrl = frontUrlGateway.getUrl();
    val uri = UriComponentsBuilder.fromUriString(frontUrl)
        .queryParam("accessToken", accessToken)
        .build()
        .toUriString();
    return new OAuth2Response(new RedirectView(uri));
  }

  @Override
  public OAuth2Response login(@NonNull String accessToken) {
    val frontUrl = frontUrlGateway.getUrl();
    val uri = UriComponentsBuilder.fromUriString(frontUrl)
        .queryParam("accessToken", accessToken)
        .build()
        .toUriString();
    return new OAuth2Response(new RedirectView(uri));
  }

}
