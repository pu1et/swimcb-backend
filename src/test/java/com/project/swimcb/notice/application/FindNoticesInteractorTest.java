package com.project.swimcb.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.db.entity.NoticeEntity;
import com.project.swimcb.db.entity.TestNoticeFactory;
import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.notice.domain.Notice;
import java.time.LocalDateTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindNoticesInteractorTest {

  @InjectMocks
  private FindNoticesInteractor findNoticesInteractor;

  @Mock
  private NoticeRepository noticeRepository;

  @Nested
  @DisplayName("공지사항 리스트 조회")
  class FindNotices {

    @Test
    @DisplayName("성공적으로 공지사항 리스트를 조회한다")
    void shouldFindNoticesSuccessfully() {
      // given
      val pageable = PageRequest.of(0, 10);
      val noticeEntities = List.of(
          createTestNoticeEntity(1L, "공지사항 제목1", "공지사항 내용1"),
          createTestNoticeEntity(2L, "공지사항 제목2", "공지사항 내용2")
      );
      val noticeEntityPage = new PageImpl<>(noticeEntities, pageable,
          noticeEntities.size());

      given(noticeRepository.findAllBySwimmingPool_IdAndIsVisibleIsTrueOrderByCreatedAtDesc(1L,
          pageable)).willReturn(noticeEntityPage);

      // when
      val result = findNoticesInteractor.findNotices(pageable);

      // then
      then(noticeRepository).should()
          .findAllBySwimmingPool_IdAndIsVisibleIsTrueOrderByCreatedAtDesc(1L, pageable);

      assertThat(result).isNotNull();
      assertThat(result.getContent()).hasSize(2);
      assertThat(result.getTotalElements()).isEqualTo(2);

      Notice firstNotice = result.getContent().get(0);
      assertThat(firstNotice.noticeId()).isEqualTo(1L);
      assertThat(firstNotice.title()).isEqualTo("공지사항 제목1");
      assertThat(firstNotice.content()).isEqualTo("공지사항 내용1");
      assertThat(firstNotice.date()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0).toLocalDate());

      Notice secondNotice = result.getContent().get(1);
      assertThat(secondNotice.noticeId()).isEqualTo(2L);
      assertThat(secondNotice.title()).isEqualTo("공지사항 제목2");
      assertThat(secondNotice.content()).isEqualTo("공지사항 내용2");
      assertThat(secondNotice.date()).isEqualTo(LocalDateTime.of(2024, 1, 1, 10, 0).toLocalDate());
    }

    @Test
    @DisplayName("빈 리스트를 반환한다")
    void shouldReturnEmptyList() {
      // given
      val pageable = PageRequest.of(0, 10);
      val emptyPage = new PageImpl<NoticeEntity>(List.of(), pageable, 0);

      given(noticeRepository.findAllBySwimmingPool_IdAndIsVisibleIsTrueOrderByCreatedAtDesc(1L,
          pageable))
          .willReturn(emptyPage);

      // when
      val result = findNoticesInteractor.findNotices(pageable);

      // then
      then(noticeRepository).should()
          .findAllBySwimmingPool_IdAndIsVisibleIsTrueOrderByCreatedAtDesc(1L, pageable);

      assertThat(result).isNotNull();
      assertThat(result.getContent()).isEmpty();
      assertThat(result.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("null pageable로 요청 시 NullPointerException이 발생한다")
    void shouldThrowNullPointerExceptionWhenPageableIsNull() {
      // given
      // when
      // then
      assertThatThrownBy(() -> findNoticesInteractor.findNotices(null))
          .isInstanceOf(NullPointerException.class);
    }

  }

  private NoticeEntity createTestNoticeEntity(Long id, String title, String content) {
    return TestNoticeFactory.create(
        id,
        title,
        content,
        true,
        LocalDateTime.of(2024, 1, 1, 10, 0),
        "test",
        LocalDateTime.of(2024, 1, 1, 10, 0),
        "test"
    );
  }

}
