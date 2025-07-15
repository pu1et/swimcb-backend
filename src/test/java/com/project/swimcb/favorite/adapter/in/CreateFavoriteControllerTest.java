package com.project.swimcb.favorite.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CreateFavoriteController.class)
class CreateFavoriteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/favorites";

  @Test
  @DisplayName("즐겨찾기 등록 성공")
  void shouldCreateFavoriteSuccessfully() throws Exception {
    // given
    val request = CreateFavoriteRequestFactory.create();

    // when & then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("targetType이 null이면 400을 반환한다")
  void shouldReturn400WhenTargetTypeIsNull() throws Exception {
    // given
    val request = CreateFavoriteRequestFactory.createWithNoTargetType();

    // when & then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("targetType은 null일 수 없습니다.")));
  }

  @Test
  @DisplayName("targetId가 null이면 400을 반환한다")
  void shouldReturn400WhenTargetIdIsNull() throws Exception {
    // given
    val request = CreateFavoriteRequestFactory.createWithNoTargetId();

    // when & then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("targetId는 null일 수 없습니다.")));
  }

  @Test
  @DisplayName("잘못된 JSON 형식이면 400을 반환한다")
  void shouldReturn400WhenInvalidJsonFormat() throws Exception {
    // given
    val invalidJson = "{ invalid json }";

    // when & then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(invalidJson))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("request가 비었으면 400을 반환한다")
  void shouldReturn400WhenEmptyRequestBody() throws Exception {
    // given & when & then
    mockMvc.perform(post(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content("{}"))
        .andExpect(status().isBadRequest());
  }

  private static class CreateFavoriteRequestFactory {

    private static CreateFavoriteRequest create() {
      return new CreateFavoriteRequest(
          FavoriteTargetType.SWIMMING_POOL,
          1L
      );
    }

    private static CreateFavoriteRequest createWithNoTargetType() {
      return new CreateFavoriteRequest(
          null,
          1L
      );
    }

    private static CreateFavoriteRequest createWithNoTargetId() {
      return new CreateFavoriteRequest(
          FavoriteTargetType.SWIMMING_POOL,
          null
      );
    }

  }

}
