package com.project.swimcb.oauth2.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.project.swimcb.oauth2.domain.enums.Environment;
import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.out.FrontUrlGateway;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

@ExtendWith(MockitoExtension.class)
class OAuth2FormatterTest {

  @InjectMocks
  private OAuth2Formatter oAuth2Formatter;

  @Mock
  private FrontUrlGateway frontUrlGateway;

  private final String frontUrl = "https://example.com";
  private final String accessToken = "valid-access-token";
  private final Environment env = Environment.LOCAL;
  private final String authLoginPath = "/auth/oauth/callback";

  @Nested
  @DisplayName("회원가입 응답 생성 시")
  class SignupMethod {

    @Test
    @DisplayName("accessToken을 포함한 리다이렉트 응답을 반환해야 한다")
    void shouldReturnRedirectResponseWithAccessToken() {
      // given
      val redirectUrl = frontUrl + authLoginPath;

      when(frontUrlGateway.getUrl(env)).thenReturn(frontUrl);

      // when
      OAuth2Response response = oAuth2Formatter.signup(accessToken, env);

      // then
      assertThat(response.redirectView()).isInstanceOf(RedirectView.class);
      assertThat(response.redirectView().getUrl())
          .isEqualTo(redirectUrl + "?accessToken=" + accessToken);
    }

    @Test
    @DisplayName("null accessToken이 주어지면 NullPointerException을 발생시켜야 한다")
    void shouldThrowNullPointerExceptionWhenGivenNullAccessToken() {
      // when
      // then
      assertThatThrownBy(() -> oAuth2Formatter.signup(null, env))
          .isInstanceOf(NullPointerException.class);
    }
  }

  @Nested
  @DisplayName("로그인 응답 생성 시")
  class LoginMethod {

    @Test
    @DisplayName("accessToken을 포함한 리다이렉트 응답을 반환해야 한다")
    void shouldReturnRedirectResponseWithAccessToken() {
      // given
      val redirectUrl = frontUrl + authLoginPath;

      when(frontUrlGateway.getUrl(env)).thenReturn(frontUrl);

      // when
      OAuth2Response response = oAuth2Formatter.login(accessToken, env);

      // then
      assertThat(response.redirectView()).isInstanceOf(RedirectView.class);
      assertThat(response.redirectView().getUrl())
          .isEqualTo(redirectUrl + "?accessToken=" + accessToken);
    }

    @Test
    @DisplayName("null accessToken이 주어지면 NullPointerException을 발생시켜야 한다")
    void shouldThrowNullPointerExceptionWhenGivenNullAccessToken() {
      // when
      // then
      assertThatThrownBy(() -> oAuth2Formatter.login(null, env))
          .isInstanceOf(NullPointerException.class);
    }
  }
}
