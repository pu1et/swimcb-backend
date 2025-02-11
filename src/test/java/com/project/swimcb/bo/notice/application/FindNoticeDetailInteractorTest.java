package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.notice.application.out.ImageUrlPrefixProvider;
import com.project.swimcb.bo.notice.domain.Notice;
import com.project.swimcb.bo.notice.domain.NoticeImage;
import com.project.swimcb.bo.notice.domain.NoticeImageRepository;
import com.project.swimcb.bo.notice.domain.NoticeRepository;
import com.project.swimcb.bo.notice.domain.TestNoticeImageFactory;
import com.project.swimcb.bo.notice.domain.TestNoticeFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindNoticeDetailInteractorTest {

  @InjectMocks
  private FindNoticeDetailInteractor interactor;

  @Mock
  private NoticeRepository noticeRepository;

  @Mock
  private NoticeImageRepository noticeImageRepository;

  @Mock
  private ImageUrlPrefixProvider imageUrlPrefixProvider;

  @Test
  @DisplayName("공지사항 ID로 상세 조회 성공")
  void shouldReturnFaqDetailWhenFaqExists() {
    // given
    val noticeId = 1L;
    val notice = Optional.of(NoticeFactory.create());
    val noticeImages = NoticeImagesFactory.create();
    val imagePrefix = "http://prefix.com/";

    when(noticeRepository.findById(any())).thenReturn(notice);
    when(noticeImageRepository.findByNoticeId(any())).thenReturn(noticeImages);
    when(imageUrlPrefixProvider.provide()).thenReturn(imagePrefix);
    // when
    val response = interactor.findDetail(noticeId);
    // then
    assertThat(response).isNotNull();
    assertThat(response.createdBy()).isEqualTo("createdBy1");
    assertThat(response.title()).isEqualTo("title1");
    assertThat(response.content()).isEqualTo("content1");
    assertThat(response.imageUrls()).containsExactly(
        "http://prefix.com/path1", "http://prefix.com/path2");
    assertThat(response.isVisible()).isTrue();
    assertThat(response.createdAt()).isEqualTo(LocalDate.of(2025, 1, 1));
  }

  @Test
  @DisplayName("존재하지 않는 공지사항 조회 시 예외 발생")
  void shouldThrowExceptionWHenNoticeNotFound() {
    // given
    val noticeId = 1L;
    when(noticeRepository.findById(any())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.findDetail(noticeId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("공지사항이 존재하지 않습니다.");

    verify(noticeRepository, only()).findById(noticeId);
    verify(noticeImageRepository, never()).findByNoticeId(any());
    verify(imageUrlPrefixProvider, never()).provide();
  }

  @Test
  @DisplayName("공지사항 이미지가 없을때는 빈 리스트 반환")
  void shouldReturnEmptyImageWhenNoImagesExist() {
    // given
    val noticeId = 1L;
    val notice = Optional.of(NoticeFactory.create());
    final List<NoticeImage> emptyImage = List.of();

    when(noticeRepository.findById(any())).thenReturn(notice);
    when(noticeImageRepository.findByNoticeId(any())).thenReturn(emptyImage);
    // when
    val response = interactor.findDetail(noticeId);
    // then
    assertThat(response).isNotNull();
    assertThat(response.imageUrls()).isEmpty();

    verify(noticeRepository, only()).findById(noticeId);
    verify(noticeImageRepository, only()).findByNoticeId(noticeId);
    verify(imageUrlPrefixProvider, never()).provide();
  }

  private static class NoticeFactory {

    private static Notice create() {
      return TestNoticeFactory.create(1L, "title1", "content1", true,
          LocalDateTime.of(2025, 1, 1, 1, 1, 1), "createdBy1",
          LocalDateTime.of(2025, 1, 2, 1, 1, 1),
          "updatedBy1");
    }
  }

  private static class NoticeImagesFactory {

    private static List<NoticeImage> create() {
      return List.of(
          TestNoticeImageFactory.create(1L, 1L, "path1",
              LocalDateTime.of(2025, 1, 1, 1, 1, 1), "createdBy1",
              LocalDateTime.of(2025, 1, 2, 1, 1, 1),
              "updatedBy1"),
          TestNoticeImageFactory.create(2L, 1L, "path2",
              LocalDateTime.of(2025, 1, 1, 1, 1, 1), "createdBy1",
              LocalDateTime.of(2025, 1, 2, 1, 1, 1),
              "updatedBy1"));
    }
  }
}