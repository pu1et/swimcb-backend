package com.project.swimcb.bo.swimmingpool.adapter.in;

import static com.project.swimcb.bo.swimmingpool.adapter.in.FindSwimmingPoolBasicInfoControllerTest.SWIMMING_POOL_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingpool.application.in.UpdateSwimmingPoolBasicInfoUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = UpdateSwimmingPoolBasicInfoController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class UpdateSwimmingPoolBasicInfoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UpdateSwimmingPoolBasicInfoUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/swimming-pools/basic-info";

  @Test
  @DisplayName("수영장 기본 정보 수정 성공")
  void shouldUpdateSwimmingPoolBasicInfoSuccessfully() throws Exception {
    // given
    val swimmingPoolId = 1L;
    val request = UpdateSwimmingPoolBasicInfoRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(PATH, swimmingPoolId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase, only()).updateBasicInfo(swimmingPoolId, request.toCommand());
  }

  @Test
  @DisplayName("존재하지 않는 수영장 수정 시 404 반환")
  void shouldReturn400WhenSwimmingPoolNotFound() throws Exception {
    // given
    val swimmingPoolId = 1L;
    val request = UpdateSwimmingPoolBasicInfoRequestFactory.create();

    doThrow(NoSuchElementException.class).when(useCase).updateBasicInfo(anyLong(), any());
    // when
    // then
    mockMvc.perform(put(PATH, swimmingPoolId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound());

    verify(useCase, only()).updateBasicInfo(swimmingPoolId, request.toCommand());
  }

  @Test
  @DisplayName("수영장 기본 정보 수정 시 images가 null이면 400 반환")
  void shouldReturn400WhenImagesIsNull() throws Exception {
    // given
    val swimmingPoolId = 1L;
    val request = UpdateSwimmingPoolBasicInfoRequestFactory.imagesIsNull();
    // when
    // then
    mockMvc.perform(put(PATH, swimmingPoolId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("images은 null일 수 없습니다.")));

    verify(useCase, never()).updateBasicInfo(anyLong(), any());
  }

  @Test
  @DisplayName("수영장 기본 정보 수정 시 images가 6개 초과되면 400 반환")
  void shouldReturn400WhenImagesIsOver6() throws Exception {
    // given
    val swimmingPoolId = 1L;
    val request = UpdateSwimmingPoolBasicInfoRequestFactory.imagesIsOver6();
    // when
    // then
    mockMvc.perform(put(PATH, swimmingPoolId)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("images는 6개 이하여야 합니다.")));

    verify(useCase, never()).updateBasicInfo(anyLong(), any());
  }

  private static class UpdateSwimmingPoolBasicInfoRequestFactory {

    public static UpdateSwimmingPoolBasicInfoRequest create() {
      return UpdateSwimmingPoolBasicInfoRequest.builder()
          .name("name")
          .phone("phone")
          .address("address")
          .imagePaths(List.of("image"))
          .usageAgreementPath("usageAgreementPath")
          .build();
    }

    public static UpdateSwimmingPoolBasicInfoRequest imagesIsNull() {
      return UpdateSwimmingPoolBasicInfoRequest.builder()
          .name("name")
          .phone("phone")
          .address("address")
          .usageAgreementPath("usageAgreementPath")
          .build();
    }

    public static UpdateSwimmingPoolBasicInfoRequest imagesIsOver6() {
      return UpdateSwimmingPoolBasicInfoRequest.builder()
          .name("name")
          .phone("phone")
          .address("address")
          .imagePaths(IntStream.range(0, 7).mapToObj(i -> "image").toList())
          .usageAgreementPath("usageAgreementPath")
          .build();
    }
  }
}