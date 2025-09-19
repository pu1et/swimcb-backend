package com.project.swimcb.oauth2.adapter.out;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.out.FrontUrlGateway;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import com.project.swimcb.oauth2.domain.enums.Environment;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
class OAuth2Formatter implements OAuth2Presenter {

  private final FrontUrlGateway frontUrlGateway;
  private final String authLoginPath = "/auth/oauth/callback";

  @Override
  public OAuth2Response signup(@NonNull String accessToken, Environment env) {
    val redirectUrl = frontUrlGateway.getUrl(env) + authLoginPath;

    val uri = UriComponentsBuilder.fromUriString(redirectUrl)
        .queryParam("accessToken", accessToken)
        .build()
        .toUriString();

    log.info("redirect url : {}", redirectUrl);

    return new OAuth2Response(new RedirectView(uri));
  }

  @Override
  public OAuth2Response login(@NonNull String accessToken, Environment env) {
    val redirectUrl = frontUrlGateway.getUrl(env) + authLoginPath;

    val uri = UriComponentsBuilder.fromUriString(redirectUrl)
        .queryParam("accessToken", accessToken)
        .build()
        .toUriString();

    log.info("redirect url : {}", redirectUrl);

    return new OAuth2Response(new RedirectView(uri));
  }

}
