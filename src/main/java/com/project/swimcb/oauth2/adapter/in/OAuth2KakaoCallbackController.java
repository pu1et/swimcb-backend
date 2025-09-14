package com.project.swimcb.oauth2.adapter.in;

import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.domain.KakaoOAuth2Request;
import com.project.swimcb.oauth2.domain.OAuth2Request;
import com.project.swimcb.oauth2.domain.enums.Environment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Tag(name = "회원")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/login/oauth2/kakao")
@RequiredArgsConstructor
public class OAuth2KakaoCallbackController {

  private final OAuth2Adapter<KakaoOAuth2Request> oAuth2Adapter;

  @Operation(summary = "카카오 콜백 처리")
  @GetMapping
  public RedirectView kakaoSuccess(
      @RequestParam(value = "code") String code,
      @RequestParam(value = "state", required = false) String state
  ) {

    return oAuth2Adapter.success(
        KakaoOAuth2Request.builder()
            .code(code)
            .env(env(state))
            .build()
    ).redirectView();
  }

  private Environment env(String state) {
    if (state == null || state.isBlank()) {
      return null;
    }
    return Environment.valueOf(state.toUpperCase());
  }

}
