package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailMainControllerTest.MEMBER_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailMainUseCase;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailMainController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindSwimmingPoolDetailMainControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindSwimmingPoolDetailMainUseCase useCase;

  @MockitoBean
  private FindSwimmingPoolDetailMainResponseFactory factory;

  static final String MEMBER_ID = "1";

  private static final String PATH = "/api/swimming-pools/1/main";
  private static final long SWIMMING_POOL_ID = 1L;

  @Test
  @DisplayName("수영장 상세 조회 - 메인정보 조회 성공")
  void shouldFindSwimmingPoolDetailMainSuccessfully() throws Exception {
    // given
    val result = TestSwimmingPoolDetailMainFactory.create();
    val response = TestFindSwimmingPoolDetailMainResponseFactory.create();

    when(useCase.findSwimmingPoolDetailMain(anyLong(), anyLong())).thenReturn(result);
    when(factory.create(any())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findSwimmingPoolDetailMain(SWIMMING_POOL_ID, Long.valueOf(MEMBER_ID));
    verify(factory, only()).create(result);
  }

  private static class TestSwimmingPoolDetailMainFactory {

    private static SwimmingPoolDetailMain create() {
      return SwimmingPoolDetailMain.builder()
          .imagePaths(List.of("DUMMY_IMAGE_PATH1", "DUMMY_IMAGE_PATH2"))
          .name("DUMMY_NAME")
          .isFavorite(true)
          .rating(4.5)
          .reviewCount(10)
          .address("DUMMY_ADDRESS")
          .phone("DUMMY_PHONE")
          .build();
    }
  }

  private static class TestFindSwimmingPoolDetailMainResponseFactory {

    private static FindSwimmingPoolDetailMainResponse create() {
      return FindSwimmingPoolDetailMainResponse.builder()
          .imageUrls(List.of("DUMMY_IMAGE_URL1", "DUMMY_IMAGE_URL2"))
          .name("DUMMY_NAME")
          .isFavorite(true)
          .rating(4.5)
          .reviewCount(10)
          .address("DUMMY_ADDRESS")
          .phone("DUMMY_PHONE")
          .build();
    }
  }
}