package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.notice.adapter.in.FindNoticesResponse;
import com.project.swimcb.bo.notice.application.out.SearchNoticesDsGateway;
import com.project.swimcb.db.entity.NoticeEntity;
import com.project.swimcb.db.entity.TestNoticeFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class SearchNoticesInteractorTest {

  @InjectMocks
  private SearchNoticesInteractor interactor;

  @Mock
  private SearchNoticesDsGateway gateway;

  @Test
  @DisplayName("공지사항 검색시 정상적으로 결과가 반환")
  void shouldReturnPagedNoticeResponsesWhenNoticesExist() {
    // given
    val keyword = "keyword";
    val pageable = Pageable.ofSize(10);

    val notices = NoticeFactory.create();
    val noticePage = new PageImpl<>(notices, pageable, notices.size());

    when(gateway.searchNotices(any(), any())).thenReturn(noticePage);
    // when
    val result = interactor.searchNotices(keyword, pageable);
    // then
    assertThat(result).hasSize(2);
    assertThat(result.getContent()).extracting(FindNoticesResponse::noticeId)
        .containsExactly(1L, 2L);

    verify(gateway, only()).searchNotices(keyword, pageable);
  }

  @Test
  @DisplayName("공지사항 검색시 결과가 없을 때 빈 페이지 반환")
  void shouldReturnEmptyPageWhenNoNoticesExist() {
    // given
    val keyword = "keyword";
    val pageable = Pageable.ofSize(10);

    when(gateway.searchNotices(any(), any())).thenReturn(Page.empty());
    // when
    val result = interactor.searchNotices(keyword, pageable);
    // then
    assertThat(result).isEmpty();

    verify(gateway, only()).searchNotices(keyword, pageable);
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
