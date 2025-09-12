package com.project.swimcb.oauth2.adapter.in;

import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.oauth2.application.port.in.OAuth2Adapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = OAuth2CallbackController.class)
@DisplayName("OAuth2CallbackController 테스트")
class OAuth2CallbackControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private OAuth2Adapter oAuth2Adapter;

  @Nested
  @DisplayName("애플 콜백 메서드")
  class AppleSuccessTest {

    @Test
    @DisplayName("유효한 애플 인증 응답으로 요청하면 성공한다")
    void shouldSucceedWithValidAppleAuthorizationResponse() throws Exception {
      // given
      var request = TestAppleAuthorizationResponseFactory.createValid();
      String requestJson = objectMapper.writeValueAsString(request);

      // when
      // then
      mockMvc.perform(post("/login/oauth2/apple")
              .contentType(APPLICATION_JSON)
              .content(requestJson))
          .andExpect(status().isOk());
    }

    @Test
    @DisplayName("빈 요청 본문으로 요청하면 400 에러가 발생한다")
    void shouldReturnBadRequestWhenRequestBodyIsEmpty() throws Exception {
      // given
      // when
      // then
      mockMvc.perform(post("/login/oauth2/apple")
              .contentType(APPLICATION_JSON)
              .content(""))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("authorization이 null이면 400 에러가 발생한다")
    void shouldReturnBadRequestWhenAuthorizationIsNull() throws Exception {
      // given
      var request = TestAppleAuthorizationResponseFactory.createWithNullAuthorization();
      String requestJson = objectMapper.writeValueAsString(request);

      // when
      // then
      mockMvc.perform(post("/login/oauth2/apple")
              .contentType(APPLICATION_JSON)
              .content(requestJson))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("user가 null이면 400 에러가 발생한다")
    void shouldReturnBadRequestWhenUserIsNull() throws Exception {
      // given
      var request = TestAppleAuthorizationResponseFactory.createWithNullUser();
      String requestJson = objectMapper.writeValueAsString(request);

      // when
      // then
      mockMvc.perform(post("/login/oauth2/apple")
              .contentType(APPLICATION_JSON)
              .content(requestJson))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("authorization.code가 null이면 400 에러가 발생한다")
    void shouldReturnBadRequestWhenAuthorizationCodeIsNull() throws Exception {
      // given
      var request = TestAppleAuthorizationResponseFactory.createWithNullAuthorizationCode();
      String requestJson = objectMapper.writeValueAsString(request);

      // when
      // then
      mockMvc.perform(post("/login/oauth2/apple")
              .contentType(APPLICATION_JSON)
              .content(requestJson))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("user.email이 null이면 400 에러가 발생한다")
    void shouldReturnBadRequestWhenUserEmailIsNull() throws Exception {
      // given
      var request = TestAppleAuthorizationResponseFactory.createWithNullUserEmail();
      String requestJson = objectMapper.writeValueAsString(request);

      // when
      // then
      mockMvc.perform(post("/login/oauth2/apple")
              .contentType(APPLICATION_JSON)
              .content(requestJson))
          .andExpect(status().isBadRequest());
    }
  }

  private static class TestAppleAuthorizationResponseFactory {

    public static AppleAuthorizationResponse createValid() {
      return new AppleAuthorizationResponse(
          new AppleAuthorizationResponse.Authorization(
              "test-authorization-code",
              "test-id-token",
              "test-state"
          ),
          new AppleAuthorizationResponse.User(
              new AppleAuthorizationResponse.Name(
                  "John",
                  "Doe"
              ),
              "john.doe@example.com"
          )
      );
    }

    public static AppleAuthorizationResponse createWithNullAuthorization() {
      return new AppleAuthorizationResponse(
          null,
          new AppleAuthorizationResponse.User(
              new AppleAuthorizationResponse.Name(
                  "John",
                  "Doe"
              ),
              "john.doe@example.com"
          )
      );
    }

    public static AppleAuthorizationResponse createWithNullUser() {
      return new AppleAuthorizationResponse(
          new AppleAuthorizationResponse.Authorization(
              "test-authorization-code",
              "test-id-token",
              "test-state"
          ),
          null
      );
    }

    public static AppleAuthorizationResponse createWithNullAuthorizationCode() {
      return new AppleAuthorizationResponse(
          new AppleAuthorizationResponse.Authorization(
              null,
              "test-id-token",
              "test-state"
          ),
          new AppleAuthorizationResponse.User(
              new AppleAuthorizationResponse.Name(
                  "John",
                  "Doe"
              ),
              "john.doe@example.com"
          )
      );
    }

    public static AppleAuthorizationResponse createWithNullUserEmail() {
      return new AppleAuthorizationResponse(
          new AppleAuthorizationResponse.Authorization(
              "test-authorization-code",
              "test-id-token",
              "test-state"
          ),
          new AppleAuthorizationResponse.User(
              new AppleAuthorizationResponse.Name(
                  "John",
                  "Doe"
              ),
              null
          )
      );
    }
  }

  // AppleAuthorizationResponse record 정의 (테스트용)
  private record AppleAuthorizationResponse(
      Authorization authorization,
      User user
  ) {

    private record Authorization(
        String code,
        String idToken,
        String state
    ) {
    }

    private record User(
        Name name,
        String email
    ) {
    }

    private record Name(
        String firstName,
        String lastName
    ) {
    }
  }
}
