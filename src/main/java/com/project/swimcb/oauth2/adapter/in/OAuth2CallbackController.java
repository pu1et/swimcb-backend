package com.project.swimcb.oauth2.adapter.in;

import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "회원")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/login/oauth2/kakao")
@RequiredArgsConstructor
public class OAuth2CallbackController {

  private final OAuth2Adapter oAuth2Adapter;

  @Operation(summary = "카카오 콜백 처리")
  @GetMapping
  public RedirectView success(
      @RequestParam(value = "code") String code
  ) {

    return oAuth2Adapter.success(
        OAuth2Request.builder()
            .code(code)
            .build()
    ).redirectView();
  }

}
