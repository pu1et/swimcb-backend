package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailFacilityControllerTest.SWIMMING_POOL_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.swimmingpool.adapter.out.FindSwimmingPoolDetailFacilityResponseMapper;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailFacilityUseCase;
import com.project.swimcb.swimmingpool.domain.FindSwimmingPoolDetailFacilityResponse;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailFacility;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailFacilityController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class FindSwimmingPoolDetailFacilityControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindSwimmingPoolDetailFacilityUseCase useCase;

  @MockitoBean
  private FindSwimmingPoolDetailFacilityResponseMapper responseMapper;

  final static long SWIMMING_POOL_ID = 1L;

  private final String PATH = "/api/swimming-pools/{swimmingPoolId}/facility";


  @Nested
  @DisplayName("수영장 시설 정보 조회")
  class FindSwimmingPoolDetailFacility {

    @Test
    @DisplayName("성공 - 유효한 수영장 ID로 시설 정보 조회")
    void success_FindFacilityWithValidId() throws Exception {
      // given
      val swimmingPoolId = 1L;
      val facility = SwimmingPoolDetailFacility.builder()
          .operatingDays("월-금")
          .closedDays("토-일")
          .newRegistrationPeriodStartDay(1)
          .newRegistrationPeriodEndDay(7)
          .reRegistrationPeriodStartDay(20)
          .reRegistrationPeriodEndDay(30)
          .build();

      val response = FindSwimmingPoolDetailFacilityResponse.builder()
          .operatingDays("월-금")
          .closedDays("토-일")
          .newRegistrationPeriodStartDay(1)
          .newRegistrationPeriodEndDay(7)
          .reRegistrationPeriodStartDay(20)
          .reRegistrationPeriodEndDay(30)
          .build();

      given(useCase.findSwimmingPoolDetailFacility(swimmingPoolId)).willReturn(facility);
      given(responseMapper.toResponse(facility)).willReturn(response);

      // when
      // then
      mockMvc.perform(get(PATH, SWIMMING_POOL_ID))
          .andExpect(status().isOk())
          .andExpect(content().string(containsString(objectMapper.writeValueAsString(response))));

      then(useCase).should().findSwimmingPoolDetailFacility(swimmingPoolId);
      then(responseMapper).should().toResponse(facility);
    }

    @Test
    @DisplayName("실패 - 잘못된 수영장 ID (0 이하)")
    void fail_InvalidSwimmingPoolId() throws Exception {
      // given
      val swimmingPoolId = 0L;

      // when
      // then
      mockMvc.perform(get(PATH, swimmingPoolId))
          .andExpect(status().isBadRequest());
    }

  }

}
