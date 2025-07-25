package com.project.swimcb.bo.notice.application.out;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.notice.adapter.in.FindNoticesResponse;
import com.project.swimcb.bo.notice.application.FindNoticesInteractor;
import com.project.swimcb.db.entity.NoticeEntity;
import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.db.entity.TestNoticeFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.val;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class FindNoticesInteractorTest {

  @InjectMocks
  private FindNoticesInteractor interactor;

  @Mock
  private NoticeRepository noticeRepository;

  @Test
  @DisplayName("공지사항 리스트 조회 성공")
  void shouldReturnPagedNoticeResponses() {
    // given
    val notices = NoticeFactory.create();
    val pageable = PageRequest.of(0, 10);
    val noticePage = new PageImpl<>(notices, pageable, notices.size());

    when(noticeRepository.findAll(any(Pageable.class))).thenReturn(noticePage);

    // when
    val result = interactor.findNotices(pageable);

    // then
    assertThat(result).isNotNull();
    AssertionsForClassTypes.assertThat(result.getTotalElements()).isEqualTo(2);

    assertThat(result.getContent())
        .extracting(FindNoticesResponse::noticeId)
        .containsExactly(1L, 2L);

    assertThat(result.getContent())
        .extracting(FindNoticesResponse::title)
        .containsExactly("title1", "title2");

    assertThat(result.getContent())
        .extracting(FindNoticesResponse::createdBy)
        .containsExactly("createdBy1", "createdBy2");

    assertThat(result.getContent())
        .extracting(FindNoticesResponse::createdAt)
        .containsExactly(LocalDate.of(2025, 1, 1),
            LocalDate.of(2025, 1, 3));

    assertThat(result.getContent())
        .extracting(FindNoticesResponse::isVisible)
        .containsExactly(true, false);

    verify(noticeRepository, only()).findAll(pageable);
  }

  @DisplayName("공지사항 리스트가 비어있을 경우, 빈 페이지 반환")
  @Test
  void shouldReturnEmptyPageWhenNoData() {
    // given
    val pageable = PageRequest.of(0, 10);
    final Page<NoticeEntity> emptyPage = Page.empty();

    when(noticeRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

    // when
    val result = interactor.findNotices(pageable);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getTotalElements()).isZero();
    assertThat(result.getContent()).isEmpty();

    verify(noticeRepository, only()).findAll(pageable);
  }

  private static class NoticeFactory {

    private static List<NoticeEntity> create() {
      return List.of(TestNoticeFactory.create(1L, "title1", "content1", true,
              LocalDateTime.of(2025, 1, 1, 1, 1, 1), "createdBy1",
              LocalDateTime.of(2025, 1, 2, 1, 1, 1),
              "updatedBy1"),
          TestNoticeFactory.create(2L, "title2", "content2", false,
              LocalDateTime.of(2025, 1, 3, 1, 1, 1), "createdBy2",
              LocalDateTime.of(2025, 1, 4, 1, 1, 1), "updatedBy2"));
    }
  }
}
