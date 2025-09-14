package com.project.swimcb.notice.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.project.swimcb.notice.adapter.in.FindNoticesResponse.NoticeResponse;
import com.project.swimcb.notice.domain.Notice;
import java.time.LocalDate;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindNoticesResponseMapperTest {

  @InjectMocks
  private FindNoticesResponseMapper findNoticesResponseMapper;

  @Nested
  @DisplayName("Notice Page를 FindNoticesResponse로 매핑")
  class ToResponse {

    @Test
    @DisplayName("성공적으로 Notice Page를 FindNoticesResponse로 매핑한다")
    void shouldMapNoticePageToFindNoticesResponseSuccessfully() {
      // given
      val pageable = PageRequest.of(0, 10);
      val notices = List.of(
          createTestNotice(1L, "공지사항 제목1", "공지사항 내용1", LocalDate.of(2024, 1, 1)),
          createTestNotice(2L, "공지사항 제목2", "공지사항 내용2", LocalDate.of(2024, 1, 2))
      );
      val noticePage = new PageImpl<>(notices, pageable, notices.size());

      // when
      val result = findNoticesResponseMapper.toResponse(noticePage);

      // then
      assertThat(result).isNotNull();
      assertThat(result.notices()).isNotNull();
      assertThat(result.notices().getContent()).hasSize(2);
      assertThat(result.notices().getTotalElements()).isEqualTo(2);
      assertThat(result.notices().getPageable()).isEqualTo(pageable);

      NoticeResponse firstResponse = result.notices().getContent().get(0);
      assertThat(firstResponse.noticeId()).isEqualTo(1L);
      assertThat(firstResponse.title()).isEqualTo("공지사항 제목1");
      assertThat(firstResponse.content()).isEqualTo("공지사항 내용1");
      assertThat(firstResponse.date()).isEqualTo(LocalDate.of(2024, 1, 1));

      NoticeResponse secondResponse = result.notices().getContent().get(1);
      assertThat(secondResponse.noticeId()).isEqualTo(2L);
      assertThat(secondResponse.title()).isEqualTo("공지사항 제목2");
      assertThat(secondResponse.content()).isEqualTo("공지사항 내용2");
      assertThat(secondResponse.date()).isEqualTo(LocalDate.of(2024, 1, 2));
    }

    @Test
    @DisplayName("빈 Notice Page를 빈 FindNoticesResponse로 매핑한다")
    void shouldMapEmptyNoticePageToEmptyFindNoticesResponse() {
      // given
      val pageable = PageRequest.of(0, 10);
      val emptyNoticePage = new PageImpl<Notice>(List.of(), pageable, 0);

      // when
      val result = findNoticesResponseMapper.toResponse(emptyNoticePage);

      // then
      assertThat(result).isNotNull();
      assertThat(result.notices()).isNotNull();
      assertThat(result.notices().getContent()).isEmpty();
      assertThat(result.notices().getTotalElements()).isEqualTo(0);
      assertThat(result.notices().getPageable()).isEqualTo(pageable);
    }

    @Test
    @DisplayName("단일 Notice를 포함한 Page를 올바르게 매핑한다")
    void shouldMapSingleNoticePageCorrectly() {
      // given
      val pageable = PageRequest.of(0, 5);
      val notice = createTestNotice(10L, "단일 공지사항", "단일 공지사항 내용", LocalDate.of(2024, 3, 15));
      val singleNoticePage = new PageImpl<>(List.of(notice), pageable, 1);

      // when
      val result = findNoticesResponseMapper.toResponse(singleNoticePage);

      // then
      assertThat(result).isNotNull();
      assertThat(result.notices()).isNotNull();
      assertThat(result.notices().getContent()).hasSize(1);
      assertThat(result.notices().getTotalElements()).isEqualTo(1);
      assertThat(result.notices().getPageable()).isEqualTo(pageable);

      NoticeResponse response = result.notices().getContent().get(0);
      assertThat(response.noticeId()).isEqualTo(10L);
      assertThat(response.title()).isEqualTo("단일 공지사항");
      assertThat(response.content()).isEqualTo("단일 공지사항 내용");
      assertThat(response.date()).isEqualTo(LocalDate.of(2024, 3, 15));
    }

    @Test
    @DisplayName("null Notice Page로 요청 시 NullPointerException이 발생한다")
    void shouldThrowNullPointerExceptionWhenNoticePageIsNull() {
      // given
      // when
      // then
      assertThatThrownBy(() -> findNoticesResponseMapper.toResponse(null))
          .isInstanceOf(NullPointerException.class);
    }

  }

  private Notice createTestNotice(Long noticeId, String title, String content, LocalDate date) {
    return Notice.builder()
        .noticeId(noticeId)
        .title(title)
        .content(content)
        .date(date)
        .build();
  }

}
