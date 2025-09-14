package com.project.swimcb.oauth2.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.oauth2.adapter.out.AppleIdTokenParser;
import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import com.project.swimcb.oauth2.domain.AppleOAuth2Request;
import java.util.Map;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.view.RedirectView;

@WebMvcTestWithoutSecurity(controllers = OAuth2AppleCallbackController.class)
class OAuth2AppleCallbackControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private AppleIdTokenParser appleIdTokenParser;

  @MockitoBean
  private OAuth2Adapter<AppleOAuth2Request> oAuth2Adapter;

  @Test
  @DisplayName("정상적인 Apple 콜백 요청을 처리하고 리다이렉트한다")
  void shouldProcessAppleCallbackSuccessfully() throws Exception {
    // given
    val code = "test-code";
    val idToken = "test-id-token";
    val state = "test-state";
    val user = "test-user";
    val email = "test@example.com";
    val redirectUrl = "https://example.com/success";

    val idTokenClaimMap = Map.<String, Object>of(
        "email", email,
        "sub", "test-subject",
        "iss", "https://appleid.apple.com"
    );

    val redirectView = new RedirectView(redirectUrl);
    val oAuth2Response = TestOAuth2ResponseFactory.create(redirectView);

    given(appleIdTokenParser.parseAndVerifyToken(idToken)).willReturn(idTokenClaimMap);
    given(oAuth2Adapter.success(any(AppleOAuth2Request.class))).willReturn(oAuth2Response);

    // when
    // then
    mockMvc.perform(post("/login/oauth2/apple")
            .param("code", code)
            .param("id_token", idToken)
            .param("state", state)
            .param("user", user))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(redirectUrl));

    then(appleIdTokenParser).should().parseAndVerifyToken(idToken);
    then(oAuth2Adapter).should().success(assertArg(request -> {
      assertThat(request).isInstanceOf(AppleOAuth2Request.class);
      assertThat(request.email()).isEqualTo(email);
    }));
  }

  @Test
  @DisplayName("필수 파라미터 code가 없으면 400 에러가 발생한다")
  void shouldReturn400WhenCodeParameterIsMissing() throws Exception {
    // given
    val idToken = "test-id-token";

    // when
    // then
    mockMvc.perform(post("/login/oauth2/apple")
            .param("id_token", idToken))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("필수 파라미터 id_token이 없으면 400 에러가 발생한다")
  void shouldReturn400WhenIdTokenParameterIsMissing() throws Exception {
    // given
    val code = "test-code";

    // when
    // then
    mockMvc.perform(post("/login/oauth2/apple")
            .param("code", code))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("선택적 파라미터 state와 user 없이도 정상 처리된다")
  void shouldProcessSuccessfullyWithoutOptionalParameters() throws Exception {
    // given
    val code = "test-code";
    val idToken = "test-id-token";
    val email = "test@example.com";
    val redirectUrl = "https://example.com/success";

    val idTokenClaimMap = Map.<String, Object>of(
        "email", email,
        "sub", "test-subject"
    );

    val redirectView = new RedirectView(redirectUrl);
    val oAuth2Response = TestOAuth2ResponseFactory.create(redirectView);

    given(appleIdTokenParser.parseAndVerifyToken(idToken)).willReturn(idTokenClaimMap);
    given(oAuth2Adapter.success(any(AppleOAuth2Request.class))).willReturn(oAuth2Response);

    // when
    // then
    mockMvc.perform(post("/login/oauth2/apple")
            .param("code", code)
            .param("id_token", idToken))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl(redirectUrl));

    then(appleIdTokenParser).should().parseAndVerifyToken(idToken);
    then(oAuth2Adapter).should().success(any(AppleOAuth2Request.class));
  }

  @Test
  @DisplayName("AppleIdTokenParser에서 예외가 발생하면 에러가 전파된다")
  void shouldPropagateExceptionFromAppleIdTokenParser() throws Exception {
    // given
    val code = "test-code";
    val invalidIdToken = "invalid-token";

    given(appleIdTokenParser.parseAndVerifyToken(invalidIdToken))
        .willThrow(new RuntimeException("invalid token"));

    // when
    // then
    mockMvc.perform(post("/login/oauth2/apple")
            .param("code", code)
            .param("id_token", invalidIdToken))
        .andExpect(status().is5xxServerError());

    then(appleIdTokenParser).should().parseAndVerifyToken(invalidIdToken);
  }

  @Test
  @DisplayName("email 클레임이 없으면 에러가 발생한다")
  void shouldThrowExceptionWhenEmailClaimIsMissing() throws Exception {
    // given
    val code = "test-code";
    val idToken = "test-id-token";

    val idTokenClaimMapWithoutEmail = Map.<String, Object>of(
        "sub", "test-subject",
        "iss", "https://appleid.apple.com"
    );

    given(appleIdTokenParser.parseAndVerifyToken(idToken))
        .willReturn(idTokenClaimMapWithoutEmail);

    // when
    // then
    mockMvc.perform(post("/login/oauth2/apple")
            .param("code", code)
            .param("id_token", idToken))
        .andExpect(status().is5xxServerError());

    then(appleIdTokenParser).should().parseAndVerifyToken(idToken);
  }

  private static class TestOAuth2ResponseFactory {

    private static OAuth2Response create(RedirectView redirectView) {
      return new OAuth2Response(redirectView);
    }

  }

}
