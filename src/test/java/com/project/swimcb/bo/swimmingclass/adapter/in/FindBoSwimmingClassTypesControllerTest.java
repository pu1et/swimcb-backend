package com.project.swimcb.bo.swimmingclass.adapter.in;

import static com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesControllerTest.SWIMMING_POOL_ID;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.application.in.FindBoSwimmingClassTypesUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindBoSwimmingClassTypesController.class)
@WithMockTokenInfo(swimmingPoolId = SWIMMING_POOL_ID)
class FindBoSwimmingClassTypesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindBoSwimmingClassTypesUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  static final long SWIMMING_POOL_ID = 1L;

  private static final String PATH = "/api/bo/swimming-classes/class-types";

  @Test
  @DisplayName("강습형태/구분 리스트 조회 성공")
  void shouldReturnResponseWhenSwimmingPoolExist() throws Exception {
    // given
    val response = FindFindBoSwimmingClassTypesResponseFactory.create();

    when(useCase.findBoSwimmingClassTypes(anyLong())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findBoSwimmingClassTypes(SWIMMING_POOL_ID);
  }

  private static class FindFindBoSwimmingClassTypesResponseFactory {

    private static FindBoSwimmingClassTypesResponse create() {
      return FindBoSwimmingClassTypesResponse.builder()
          .classTypes(List.of(
              FindBoSwimmingClassTypesResponse.ClassType.builder()
                  .classTypeId(1L)
                  .name("DUMMY_NAME1")
                  .classSubTypes(List.of(
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(1L)
                          .name("DUMMY_NAME1")
                          .build(),
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(2L)
                          .name("DUMMY_NAME2")
                          .build()
                  ))
                  .build(),
              FindBoSwimmingClassTypesResponse.ClassType.builder()
                  .classTypeId(2L)
                  .name("DUMMY_NAME2")
                  .classSubTypes(List.of(
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(3L)
                          .name("DUMMY_NAME3")
                          .build(),
                      FindBoSwimmingClassTypesResponse.ClassSubType.builder()
                          .classSubTypeId(4L)
                          .name("DUMMY_NAME4")
                          .build()
                  ))
                  .build()
          ))
          .build();
    }
  }
}