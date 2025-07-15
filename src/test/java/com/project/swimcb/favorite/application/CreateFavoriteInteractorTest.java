package com.project.swimcb.favorite.application;

import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.FREE_SWIMMING;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.db.repository.FavoriteRepository;
import com.project.swimcb.favorite.adapter.in.CreateFavoriteCommand;
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

  @Test
  @DisplayName("회원이 특정 타입의 즐겨찾기를 등록할 때, 해당 즐겨찾기가 저장되어야 한다")
  void shouldCreateFavoriteSuccessfully() {
    // given
    val memberId = 1L;
    val targetType = FREE_SWIMMING;
    val targetId = 100L;
    val command = CreateFavoriteCommand.builder()
        .memberId(memberId)
        .targetType(targetType)
        .targetId(targetId)
        .build();

    // when
    createFavoriteInteractor.createFavorite(command);

    // then
    then(favoriteRepository).should().save(argThat(i ->
        i.getMember().getId() == memberId
            && i.getTargetType().equals(targetType)
            && i.getTargetId() == targetId)
    );
  }

}
