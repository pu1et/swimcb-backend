package com.project.swimcb.oauth2.adapter.in;

import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.domain.enums.Environment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Tag(name = "회원")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/login/oauth2")
@RequiredArgsConstructor
public class OAuth2CallbackController {

  private final OAuth2Adapter oAuth2Adapter;

  @Operation(summary = "카카오 콜백 처리")
  @GetMapping("/kakao")
  public RedirectView kakaoSuccess(
      @RequestParam(value = "code") String code,
      @RequestParam(value = "state", required = false) String state
  ) {

    return oAuth2Adapter.success(
        OAuth2Request.builder()
            .code(code)
            .env(env(state))
            .build()
    ).redirectView();
  }

  @Operation(summary = "애플 콜백 처리")
  @PostMapping("/apple")
  public RedirectView appleSuccess(
      @RequestParam(value = "code") String code,
      @RequestParam(value = "id_token") String idToken,
      @RequestParam(value = "state", required = false) String state,
      @RequestParam(value = "user", required = false) String user
  ) {

    log.info("Apple callback received");
    log.info("code={}", code);
    log.info("id_token={}", idToken);
    log.info("state={}", state);
    log.info("user={}", user);

    return null;
  }

  private Environment env(String state) {
    if (state == null || state.isBlank()) {
      return null;
    }
    return Environment.valueOf(state.toUpperCase());
  }

}
