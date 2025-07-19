package com.project.swimcb.favorite.application;

import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.FREE_SWIMMING;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_CLASS;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.project.swimcb.db.entity.FreeSwimmingDayStatusEntity;
import com.project.swimcb.db.entity.SwimmingClassEntity;
import com.project.swimcb.db.entity.SwimmingPoolEntity;
import com.project.swimcb.db.repository.FavoriteRepository;
import com.project.swimcb.db.repository.FreeSwimmingDayStatusRepository;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
import com.project.swimcb.favorite.adapter.in.CreateFavoriteCommand;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("회원 즐겨찾기 등록")
@ExtendWith(MockitoExtension.class)
class CreateFavoriteInteractorTest {

  @InjectMocks
  private CreateFavoriteInteractor createFavoriteInteractor;

  @Mock
  private FavoriteRepository favoriteRepository;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Mock
  private SwimmingClassRepository swimmingClassRepository;

  @Mock
  private FreeSwimmingDayStatusRepository freeSwimmingDayStatusRepository;

  @Test
  @DisplayName("회원이 수영장 즐겨찾기를 등록할 때, 해당 즐겨찾기가 저장되어야 한다")
  void shouldCreateSwimmingPoolFavoriteSuccessfully() {
    // given
    val memberId = 1L;
    val targetType = SWIMMING_POOL;
    val targetId = 100L;
    val command = CreateFavoriteCommand.builder()
        .memberId(memberId)
        .targetType(targetType)
        .targetId(targetId)
        .build();
    val findSwimmingPoolEntity = Optional.of(mock(SwimmingPoolEntity.class));

    given(swimmingPoolRepository.findById(targetId)).willReturn(findSwimmingPoolEntity);

    // when
    createFavoriteInteractor.createFavorite(command);

    // then
    then(favoriteRepository).should().save(argThat(i ->
        i.getMember().getId() == memberId
            && i.getTargetType().equals(targetType)
            && i.getTargetId() == targetId)
    );
  }

  @Test
  @DisplayName("회원이 수영강습 즐겨찾기를 등록할 때, 해당 즐겨찾기가 저장되어야 한다")
  void shouldCreateSwimmingClassFavoriteSuccessfully() {
    // given
    val memberId = 1L;
    val targetType = SWIMMING_CLASS;
    val targetId = 100L;
    val command = CreateFavoriteCommand.builder()
        .memberId(memberId)
        .targetType(targetType)
        .targetId(targetId)
        .build();
    val findSwimmingClassEntity = Optional.of(mock(SwimmingClassEntity.class));

    given(swimmingClassRepository.findById(targetId)).willReturn(findSwimmingClassEntity);

    // when
    createFavoriteInteractor.createFavorite(command);

    // then
    then(favoriteRepository).should().save(argThat(i ->
        i.getMember().getId() == memberId
            && i.getTargetType().equals(targetType)
            && i.getTargetId() == targetId)
    );
  }

  @Test
  @DisplayName("회원이 자유수영 즐겨찾기를 등록할 때, 해당 즐겨찾기가 저장되어야 한다")
  void shouldCreateFreeSwimmingFavoriteSuccessfully() {
    // given
    val memberId = 1L;
    val targetType = FREE_SWIMMING;
    val targetId = 100L;
    val command = CreateFavoriteCommand.builder()
        .memberId(memberId)
        .targetType(targetType)
        .targetId(targetId)
        .build();
    val findFreeSwimmingDayStatusEntity = Optional.of(mock(FreeSwimmingDayStatusEntity.class));

    given(freeSwimmingDayStatusRepository.findById(targetId)).willReturn(
        findFreeSwimmingDayStatusEntity);

    // when
    createFavoriteInteractor.createFavorite(command);

    // then
    then(favoriteRepository).should().save(argThat(i ->
        i.getMember().getId() == memberId
            && i.getTargetType().equals(targetType)
            && i.getTargetId() == targetId)
    );
  }

  @Test
  @DisplayName("존재하지 않는 수영장 즐겨찾기 등록시 예외가 발생해야 한다")
  void shouldThrowExceptionWhenSwimmingPoolNotExists() {
    // given
    val command = CreateFavoriteCommand.builder()
        .memberId(1L)
        .targetType(SWIMMING_POOL)
        .targetId(999L)
        .build();

    given(swimmingPoolRepository.findById(999L)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> createFavoriteInteractor.createFavorite(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 수영장 정보가 존재하지 않습니다.");
  }

  @Test
  @DisplayName("존재하지 않는 수영강습 즐겨찾기 등록시 예외가 발생해야 한다")
  void shouldThrowExceptionWhenSwimmingClassNotExists() {
    // given
    val command = CreateFavoriteCommand.builder()
        .memberId(1L)
        .targetType(SWIMMING_CLASS)
        .targetId(999L)
        .build();

    given(swimmingClassRepository.findById(999L)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> createFavoriteInteractor.createFavorite(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 수영강습 정보가 존재하지 않습니다.");
  }

  @Test
  @DisplayName("존재하지 않는 자유수영 즐겨찾기 등록시 예외가 발생해야 한다")
  void shouldThrowExceptionWhenFreeSwimmingNotExists() {
    // given
    val command = CreateFavoriteCommand.builder()
        .memberId(1L)
        .targetType(FREE_SWIMMING)
        .targetId(999L)
        .build();

    given(freeSwimmingDayStatusRepository.findById(999L)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> createFavoriteInteractor.createFavorite(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("해당 자유수영 정보가 존재하지 않습니다.");
  }

}
