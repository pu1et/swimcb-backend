package com.project.swimcb.bo.notice.adapter.in;

import static com.project.swimcb.bo.notice.adapter.in.DeleteBoNoticesControllerTest.SWIMMING_POOL_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.notice.application.in.DeleteBoNoticesUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = DeleteBoNoticesController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class DeleteBoNoticesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DeleteBoNoticesUseCase deleteBoNoticesUseCase;

  @Autowired
  private ObjectMapper objectMapper;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/notices";

  @Test
  @DisplayName("정상적인 공지사항 삭제 요청을 처리한다")
  void shouldDeleteNoticesSuccessfully() throws Exception {
    // given
    val request = createDeleteBoNoticeRequest();

    // when
    // then
    mockMvc.perform(delete(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    then(deleteBoNoticesUseCase).should().deleteAll(request.noticeIds());
  }

  @Test
  @DisplayName("단일 공지사항 삭제 요청을 처리한다")
  void shouldDeleteSingleNoticeSuccessfully() throws Exception {
    // given
    val request = new DeleteBoNoticeRequest(List.of(1L));

    // when
    // then
    mockMvc.perform(delete(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    then(deleteBoNoticesUseCase).should().deleteAll(List.of(1L));
  }

  @Test
  @DisplayName("빈 공지사항 ID 리스트로 요청하면 400 에러가 발생한다")
  void shouldReturn400WhenNoticeIdsIsEmpty() throws Exception {
    // given
    val request = new DeleteBoNoticeRequest(List.of());

    // when
    // then
    mockMvc.perform(delete(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("noticeIds는 1개 이상이어야 합니다")));

    then(deleteBoNoticesUseCase).should(never()).deleteAll(any());
  }

  @Test
  @DisplayName("빈 요청 본문으로 요청하면 400 에러가 발생한다")
  void shouldReturn400WhenRequestBodyIsEmpty() throws Exception {
    // given
    val emptyRequestBody = "";

    // when
    // then
    mockMvc.perform(delete(PATH)
            .contentType(APPLICATION_JSON_VALUE)
            .content(emptyRequestBody))
        .andExpect(status().isBadRequest());

    then(deleteBoNoticesUseCase).should(never()).deleteAll(any());
  }

  private DeleteBoNoticeRequest createDeleteBoNoticeRequest() {
    return new DeleteBoNoticeRequest(List.of(1L, 2L, 3L));
  }

}
