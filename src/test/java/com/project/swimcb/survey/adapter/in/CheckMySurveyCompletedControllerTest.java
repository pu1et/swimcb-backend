package com.project.swimcb.survey.adapter.in;

import static com.project.swimcb.survey.adapter.in.CheckMySurveyCompletedControllerTest.MEMBER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.survey.application.port.in.CheckMySurveyCompletedUseCase;
import com.project.swimcb.survey.domain.CheckMySurveyCompleted;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = CheckMySurveyCompletedController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class CheckMySurveyCompletedControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private CheckMySurveyCompletedUseCase useCase;

  static final long MEMBER_ID = 1L;

  private static final String PATH = "/api/surveys/me/completed";

  @Nested
  @DisplayName("사용자 설문 완료 여부 조회 API는")
  class CheckMySurveyCompletedApi {

    @Test
    @DisplayName("UseCase를 호출하고 결과를 Response로 변환하여 반환한다")
    void shouldCallUseCaseAndConvertToResponse() throws Exception {
      // given
      val useCaseResult = new CheckMySurveyCompleted(true);
      given(useCase.checkMySurveyCompleted(MEMBER_ID)).willReturn(useCaseResult);

      // when
      // then
      mockMvc.perform(get(PATH)
              .contentType(APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(content().json(objectMapper.writeValueAsString(useCaseResult)));

      then(useCase).should(only()).checkMySurveyCompleted(assertArg(memberId -> {
        assertThat(memberId).isEqualTo(MEMBER_ID);
      }));
    }

  }

}
