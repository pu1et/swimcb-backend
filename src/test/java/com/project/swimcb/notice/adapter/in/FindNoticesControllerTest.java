package com.project.swimcb.notice.adapter.in;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.notice.adapter.out.FindNoticesResponseMapper;
import com.project.swimcb.notice.application.port.in.FindNoticesUseCase;
import com.project.swimcb.notice.domain.Notice;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindNoticesController.class)
class FindNoticesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindNoticesUseCase findNoticesUseCase;

  @MockitoBean
  private FindNoticesResponseMapper findNoticesResponseMapper;

  private final String PATH = "/api/notices";

  @Nested
  @DisplayName("공지사항 리스트 조회")
  class FindNotices {

    @Test
    @DisplayName("성공적으로 공지사항 리스트를 조회한다")
    void shouldFindNoticesSuccessfully() throws Exception {
      // given
      val notices = new PageImpl<Notice>(List.of());
      val expectedResponse = new FindNoticesResponse(new PageImpl<>(List.of()));

      given(findNoticesUseCase.findNotices(any(Pageable.class))).willReturn(notices);
      given(findNoticesResponseMapper.toResponse(notices)).willReturn(expectedResponse);

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("page", "1")
              .param("size", "10"))
          .andExpect(status().isOk());

      then(findNoticesUseCase).should().findNotices(assertArg(i -> {
        assertThat(i.getPageNumber()).isEqualTo(0); // page - 1
        assertThat(i.getPageSize()).isEqualTo(10);
      }));
      then(findNoticesResponseMapper).should().toResponse(notices);
    }

    @Test
    @DisplayName("기본값으로 공지사항 리스트를 조회한다")
    void shouldFindNoticesWithDefaultValues() throws Exception {
      // given
      val notices = new PageImpl<Notice>(List.of());
      val expectedResponse = new FindNoticesResponse(new PageImpl<>(List.of()));

      given(findNoticesUseCase.findNotices(any(Pageable.class))).willReturn(notices);
      given(findNoticesResponseMapper.toResponse(notices)).willReturn(expectedResponse);

      // when
      // then
      mockMvc.perform(get(PATH))
          .andExpect(status().isOk());

      then(findNoticesUseCase).should().findNotices(assertArg(i -> {
        assertThat(i.getPageNumber()).isEqualTo(0); // 기본값 1 - 1
        assertThat(i.getPageSize()).isEqualTo(10); // 기본값 10
      }));
      then(findNoticesResponseMapper).should().toResponse(notices);
    }

    @Test
    @DisplayName("커스텀 페이지 파라미터로 공지사항 리스트를 조회한다")
    void shouldFindNoticesWithCustomPageParameters() throws Exception {
      // given
      val notices = new PageImpl<Notice>(List.of());
      val expectedResponse = new FindNoticesResponse(new PageImpl<>(List.of()));

      given(findNoticesUseCase.findNotices(any(Pageable.class))).willReturn(notices);
      given(findNoticesResponseMapper.toResponse(notices)).willReturn(expectedResponse);

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("page", "3")
              .param("size", "20"))
          .andExpect(status().isOk());

      then(findNoticesUseCase).should().findNotices(assertArg(i -> {
        assertThat(i.getPageNumber()).isEqualTo(2); // page 3 - 1
        assertThat(i.getPageSize()).isEqualTo(20);
      }));
      then(findNoticesResponseMapper).should().toResponse(notices);
    }

  }

}
