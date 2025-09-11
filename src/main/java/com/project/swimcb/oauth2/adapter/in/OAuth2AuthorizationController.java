package com.project.swimcb.oauth2.adapter.in;

import static com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType.APPLE;
import static com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType.KAKAO;

import com.project.swimcb.oauth2.application.OAuth2ClientInfoProviderFactory;
import com.project.swimcb.oauth2.domain.enums.Environment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "회원")
@RestController
@RequestMapping("/api/oauth2/authorization")
@RequiredArgsConstructor
public class OAuth2AuthorizationController {

  private final OAuth2ClientInfoProviderFactory oAuth2ClientInfoProviderFactory;

  @Operation(summary = "카카오 회원가입/로그인")
  @GetMapping("/kakao")
  public RedirectView kakao(
      @RequestParam(value = "env", required = false) Environment env
  ) {
    val redirectUri = redirectUriForKakao(env);
    return new RedirectView(redirectUri);
  }

  @Operation(summary = "애플 회원가입/로그인")
  @GetMapping("/apple")
  public RedirectView apple() {
    val redirectUri = redirectUriForApple();
    return new RedirectView(redirectUri);
  }

  private String redirectUriForKakao(Environment env) {
    val info = oAuth2ClientInfoProviderFactory.getProvider(KAKAO).getOAuth2ClientInfo();

    return UriComponentsBuilder
        .fromUriString(info.provider().authorizationUri())
        .queryParam("response_type", "code")
        .queryParam("client_id", info.registration().clientId())
        .queryParam("redirect_uri", info.registration().redirectUri())
        .queryParam("scope", String.join(",", info.registration().scope()))
        .queryParam("state", env)
        .build()
        .toUriString();
  }

  private String redirectUriForApple() {
    val info = oAuth2ClientInfoProviderFactory.getProvider(APPLE).getOAuth2ClientInfo();

    return UriComponentsBuilder
        .fromUriString(info.provider().authorizationUri())
        .queryParam("response_type", "code id_token")
        .queryParam("response_mode", "form_post")
        .queryParam("client_id", info.registration().clientId())
        .queryParam("redirect_uri", info.registration().redirectUri())
        .queryParam("scope", String.join(" ", info.registration().scope()))
        .build()
        .toUriString();
  }

}
