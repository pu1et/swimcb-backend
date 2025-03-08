package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.DeleteBoSwimmingClassSubTypeControllerTest.SWIMMING_POOL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.application.in.DeleteBoSwimmingClassSubTypeUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = DeleteBoSwimmingClassSubTypeController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class DeleteBoSwimmingClassSubTypeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private DeleteBoSwimmingClassSubTypeUseCase deleteBoSwimmingClassSubTypeUseCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/swimming-classes/class-types/2/class-sub-types/3";
  private static final long CLASS_TYPE_ID = 2L;
  private static final long CLASS_SUB_TYPE_ID = 3L;

  @Test
  @DisplayName("강습구분 단건 삭제 성공")
  void shouldDeleteSuccessfully() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(delete(PATH))
        .andExpect(status().isOk());

    verify(deleteBoSwimmingClassSubTypeUseCase, only()).deleteBoSwimmingClassSubType(
        assertArg(i -> {
          assertThat(i.swimmingPoolId()).isEqualTo(SWIMMING_POOL_ID);
          assertThat(i.swimmingClassTypeId()).isEqualTo(CLASS_TYPE_ID);
          assertThat(i.swimmingClassSubTypeId()).isEqualTo(CLASS_SUB_TYPE_ID);
        }));
  }
}