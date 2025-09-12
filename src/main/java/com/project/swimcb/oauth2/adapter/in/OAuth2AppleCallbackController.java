package com.project.swimcb.oauth2.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@Tag(name = "회원")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/login/oauth2/apple")
@RequiredArgsConstructor
public class OAuth2AppleCallbackController {

  @Operation(summary = "애플 콜백 처리")
  @PostMapping
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

}
