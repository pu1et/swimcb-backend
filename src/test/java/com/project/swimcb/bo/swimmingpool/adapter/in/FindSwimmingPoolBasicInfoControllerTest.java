package com.project.swimcb.bo.swimmingpool.adapter.in;

import static com.project.swimcb.bo.swimmingpool.adapter.in.FindSwimmingPoolBasicInfoControllerTest.SWIMMING_POOL_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingpool.application.in.FindSwimmingPoolBasicInfoUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolBasicInfoController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class FindSwimmingPoolBasicInfoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindSwimmingPoolBasicInfoUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/swimming-pools/basic-info";

  static final String SWIMMING_POOL_ID = "1";

  @Test
  @DisplayName("수영장 기본 정보 조회 성공")
  void shouldReturnResponseWhenSwimmingPoolExist() throws Exception {
    // given
    val response = FindSwimmingPoolBasicInfoResponseFactory.create();

    when(useCase.findSwimmingPoolBasicInfo(anyLong())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findSwimmingPoolBasicInfo(Long.parseLong(SWIMMING_POOL_ID));
  }

  @Test
  @DisplayName("수영장 정보 없을시 400 에러 반환")
  void shouldReturn400WhenSwimmingPoolDoesNotExist() throws Exception {
    // given
    when(useCase.findSwimmingPoolBasicInfo(anyLong())).thenThrow(IllegalArgumentException.class);
    // when
    // then
    mockMvc.perform(get(PATH))
        .andExpect(status().isBadRequest());

    verify(useCase, only()).findSwimmingPoolBasicInfo(Long.parseLong(SWIMMING_POOL_ID));
  }

  private static class FindSwimmingPoolBasicInfoResponseFactory {

    private static FindSwimmingPoolBasicInfoResponse create() {
      return FindSwimmingPoolBasicInfoResponse.builder()
          .name("DUMMY_NAME")
          .phone("DUMMY_PHONE")
          .address("DUMMY_ADDRESS")
          .newRegistrationPeriodStartDay(1)
          .newRegistrationPeriodEndDay(10)
          .reRegistrationPeriodStartDay(15)
          .reRegistrationPeriodEndDay(20)
          .representativeImageUrls(List.of("DUMMY_IMAGE_URL"))
          .usageAgreementUrl("DUMMY_USAGE_AGREEMENT_URL")
          .build();
    }
  }
}