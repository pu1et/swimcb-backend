package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.DeleteBoSwimmingClassControllerTest.SWIMMING_POOL_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.bo.swimmingclass.application.in.DeleteBoSwimmingClassUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = DeleteBoSwimmingClassController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class DeleteBoSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DeleteBoSwimmingClassUseCase useCase;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/swimming-classes/2";
  private static final long SWIMMING_CLASS_ID = 2L;

  @Test
  @DisplayName("클래스 데이터 관리 - 클래스 삭제 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(delete(PATH))
        .andExpect(status().isOk());

    verify(useCase).deleteBoSwimmingClass(assertArg(i -> {
      assertThat(i.swimmingPoolId()).isEqualTo(SWIMMING_POOL_ID);
      assertThat(i.swimmingClassId()).isEqualTo(SWIMMING_CLASS_ID);
    }));
  }
}