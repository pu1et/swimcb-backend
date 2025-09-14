package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

import com.project.swimcb.bo.notice.domain.CreateBoNoticeCommand;
import com.project.swimcb.db.entity.NoticeEntity;
import com.project.swimcb.db.entity.SwimmingPoolEntity;
import com.project.swimcb.db.repository.NoticeRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
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
class CreateBoNoticeInteractorTest {

  @InjectMocks
  private CreateBoNoticeInteractor createBoNoticeInteractor;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Mock
  private NoticeRepository noticeRepository;

  @Test
  @DisplayName("정상적인 공지사항 생성 요청 시 공지사항을 생성한다")
  void shouldCreateNoticeSuccessfully() {
    // given
    val command = createCreateBoNoticeCommand();
    val swimmingPool = mock(SwimmingPoolEntity.class);
    val savedNotice = mock(NoticeEntity.class);

    given(swimmingPoolRepository.findById(command.swimmingPoolId()))
        .willReturn(Optional.of(swimmingPool));
    given(noticeRepository.save(any(NoticeEntity.class)))
        .willReturn(savedNotice);

    // when
    createBoNoticeInteractor.createNotice(command);

    // then
    then(swimmingPoolRepository).should().findById(command.swimmingPoolId());
    then(noticeRepository).should().save(assertArg(notice -> {
      assertThat(notice.getTitle()).isEqualTo(command.title());
      assertThat(notice.getContent()).isEqualTo(command.content());
      assertThat(notice.isVisible()).isEqualTo(command.isVisible());
      assertThat(notice.getSwimmingPool()).isEqualTo(swimmingPool);
    }));
  }

  @Test
  @DisplayName("존재하지 않는 수영장 ID로 요청 시 NoSuchElementException이 발생한다")
  void shouldThrowNoSuchElementExceptionWhenSwimmingPoolNotFound() {
    // given
    val command = createCreateBoNoticeCommand();
    val swimmingPoolId = command.swimmingPoolId();

    given(swimmingPoolRepository.findById(swimmingPoolId))
        .willReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> createBoNoticeInteractor.createNotice(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("수영장이 존재하지 않습니다 : " + swimmingPoolId);

    then(swimmingPoolRepository).should().findById(swimmingPoolId);
    then(noticeRepository).should(never()).save(any(NoticeEntity.class));
  }

  @Test
  @DisplayName("null command로 요청 시 NullPointerException이 발생한다")
  void shouldThrowNullPointerExceptionWhenCommandIsNull() {
    // given
    CreateBoNoticeCommand nullCommand = null;

    // when
    // then
    assertThatThrownBy(() -> createBoNoticeInteractor.createNotice(nullCommand))
        .isInstanceOf(NullPointerException.class);

    then(swimmingPoolRepository).should(never()).findById(any());
    then(noticeRepository).should(never()).save(any(NoticeEntity.class));
  }

  @Test
  @DisplayName("수영장 조회 성공 후 NoticeRepository 저장 호출을 검증한다")
  void shouldVerifyRepositoryInteractions() {
    // given
    val command = createCreateBoNoticeCommand();
    val swimmingPool = mock(SwimmingPoolEntity.class);

    given(swimmingPoolRepository.findById(command.swimmingPoolId()))
        .willReturn(Optional.of(swimmingPool));

    // when
    createBoNoticeInteractor.createNotice(command);

    // then
    then(swimmingPoolRepository).should().findById(command.swimmingPoolId());
    then(noticeRepository).should().save(any(NoticeEntity.class));
  }

  @Test
  @DisplayName("isVisible이 false인 공지사항도 정상적으로 생성된다")
  void shouldCreateNoticeWithVisibilityFalse() {
    // given
    val command = CreateBoNoticeCommand.builder()
        .swimmingPoolId(1L)
        .createdBy("테스트 관리자")
        .title("비공개 공지사항")
        .content("비공개 공지사항 내용")
        .imagePaths(List.of("image1.jpg"))
        .isVisible(false)
        .build();
    val swimmingPool = mock(SwimmingPoolEntity.class);

    given(swimmingPoolRepository.findById(command.swimmingPoolId()))
        .willReturn(Optional.of(swimmingPool));

    // when
    createBoNoticeInteractor.createNotice(command);

    // then
    then(swimmingPoolRepository).should().findById(command.swimmingPoolId());
    then(noticeRepository).should().save(any(NoticeEntity.class));
  }

  @Test
  @DisplayName("빈 이미지 경로 리스트로도 공지사항이 정상적으로 생성된다")
  void shouldCreateNoticeWithEmptyImagePaths() {
    // given
    val command = CreateBoNoticeCommand.builder()
        .swimmingPoolId(1L)
        .createdBy("테스트 관리자")
        .title("이미지 없는 공지사항")
        .content("이미지 없는 공지사항 내용")
        .imagePaths(List.of())
        .isVisible(true)
        .build();
    val swimmingPool = mock(SwimmingPoolEntity.class);

    given(swimmingPoolRepository.findById(command.swimmingPoolId()))
        .willReturn(Optional.of(swimmingPool));

    // when
    createBoNoticeInteractor.createNotice(command);

    // then
    then(swimmingPoolRepository).should().findById(command.swimmingPoolId());
    then(noticeRepository).should().save(any(NoticeEntity.class));
  }

  private CreateBoNoticeCommand createCreateBoNoticeCommand() {
    return CreateBoNoticeCommand.builder()
        .swimmingPoolId(1L)
        .createdBy("테스트 관리자")
        .title("테스트 공지사항")
        .content("테스트 공지사항 내용입니다.")
        .imagePaths(List.of("image1.jpg", "image2.jpg"))
        .isVisible(true)
        .build();
  }

}
