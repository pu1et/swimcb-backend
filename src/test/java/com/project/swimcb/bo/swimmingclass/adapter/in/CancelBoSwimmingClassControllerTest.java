package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.CancelBoSwimmingClassControllerTest.SWIMMING_POOL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.application.in.CancelBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.CancelBoSwimmingClassCommand;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CancelBoSwimmingClassController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class CancelBoSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CancelBoSwimmingClassUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/swimming-classes/2/cancel";
  private static final long SWIMMING_CLASS_ID = 2L;

  @Test
  @DisplayName("클래스 데이터 관리 - 클래스 삭제 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    val request = CancelBoSwimmingClassCommand.builder()
        .swimmingPoolId(SWIMMING_POOL_ID)
        .swimmingClassId(SWIMMING_CLASS_ID)
        .cancelReason("DUMMY_CANCEL_REASON")
        .build();
    // when
    // then
    mockMvc.perform(patch(PATH)
        .contentType(APPLICATION_JSON_VALUE)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(useCase).cancelBoSwimmingClass(assertArg(i -> {
      assertThat(i.swimmingPoolId()).isEqualTo(request.swimmingPoolId());
      assertThat(i.swimmingClassId()).isEqualTo(request.swimmingClassId());
      assertThat(i.cancelReason()).isEqualTo(request.cancelReason());
    }));
  }
}
