package com.project.swimcb.favorite.application;

import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import com.project.swimcb.db.entity.FavoriteEntity;
import com.project.swimcb.db.repository.FavoriteRepository;
import com.project.swimcb.favorite.application.in.DeleteFavoriteCommand;
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
class DeleteFavoriteInteractorTest {

  @InjectMocks
  private DeleteFavoriteInteractor interactor;

  @Mock
  private FavoriteRepository repository;

  @Test
  @DisplayName("유효한 회원 ID와 즐겨찾기 ID가 주어졌을 때 즐겨찾기가 삭제되어야 한다")
  void givenValidMemberIdAndFavoriteId_whenDeleteFavorite_thenFavoriteShouldBeDeleted() {
    // given
    val memberId = 1L;
    val targetType = SWIMMING_POOL;
    val targetId = 2L;
    val favoriteId = 100L;
    val command = DeleteFavoriteCommand.builder()
        .memberId(memberId)
        .targetType(SWIMMING_POOL)
        .targetId(targetId)
        .build();

    val favoriteEntity = mock(FavoriteEntity.class);
    when(favoriteEntity.getId()).thenReturn(favoriteId);
    when(repository.findByMember_IdAndTargetTypeAndTargetId(memberId, targetType, targetId))
        .thenReturn(Optional.of(favoriteEntity));

    // when
    interactor.deleteFavorite(command);

    // then
    then(repository).should()
        .findByMember_IdAndTargetTypeAndTargetId(memberId, targetType, targetId);
    then(repository).should().deleteById(favoriteId);
  }

  @Test
  @DisplayName("존재하지 않는 즐겨찾기 ID가 주어졌을 때 NoSuchElementException이 발생해야 한다")
  void givenNonExistentFavoriteId_whenDeleteFavorite_thenShouldThrowNoSuchElementException() {
    // given
    val memberId = 1L;
    val targetType = SWIMMING_POOL;
    val targetId = 2L;
    val favoriteId = 999L;
    val command = DeleteFavoriteCommand.builder()
        .memberId(memberId)
        .targetType(targetType)
        .targetId(targetId)
        .build();

    when(repository.findByMember_IdAndTargetTypeAndTargetId(memberId, targetType, targetId))
        .thenReturn(Optional.empty());

    // when
    // then
    assertThatThrownBy(() -> interactor.deleteFavorite(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("즐겨찾기를 찾을 수 없습니다");

    then(repository).should()
        .findByMember_IdAndTargetTypeAndTargetId(memberId, targetType, targetId);
    then(repository).should(never()).deleteById(any());
  }

}
