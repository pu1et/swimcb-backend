package com.project.swimcb.oauth2.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "회원")
@RestController
@RequestMapping("/api/oauth2/authorization/kakao")
public class OAuth2AuthorizationController {

  @Operation(summary = "카카오 회원가입/로그인")
  @GetMapping
  public RedirectView kakao() {
    return new RedirectView("카카오 회원가입/로그인 URL");
  }
}
