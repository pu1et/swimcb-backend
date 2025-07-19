package com.project.swimcb.favorite.application;

import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.when;

import com.project.swimcb.favorite.application.out.FindFavoriteDsGateway;
import com.project.swimcb.favorite.domain.Favorite;
import com.project.swimcb.favorite.domain.FindFavoriteCondition;
import com.project.swimcb.favorite.domain.SwimmingPoolFavorite;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindFavoriteInteractorTest {

  @InjectMocks
  private FindFavoriteInteractor interactor;

  @Mock
  private FindFavoriteDsGateway gateway;

  @Test
  @DisplayName("유효한 회원 ID가 주어졌을 때 해당 회원의 즐겨찾기 목록이 반환되어야 한다")
  void givenValidMemberId_whenFindFavorites_thenShouldReturnFavoriteList() {
    // given
    val pageable = PageRequest.of(1, 10);
    val condition = FindFavoriteCondition.builder()
        .memberId(1L)
        .memberLatitude(37.5665)
        .memberLongitude(126.978)
        .pageable(pageable)
        .build();

    val favorite = createFavorite();
    val page = new PageImpl<Favorite>(List.of(favorite), pageable, 1);
    when(gateway.findFavorites(any(FindFavoriteCondition.class))).thenReturn(page);

    // when
    val result = interactor.findFavorites(condition);

    // then
    assertThat(result).isEqualTo(page);

    then(gateway).should(only()).findFavorites(condition);
  }

  @Test
  @DisplayName("null 회원 ID가 주어졌을 때 예외가 발생해야 한다")
  void givenNullMemberId_whenFindFavorites_thenShouldThrowException() {
    // given
    val condition = (FindFavoriteCondition) null;

    // when & then
    assertThrows(Exception.class, () -> {
      interactor.findFavorites(condition);
    });
  }

  private SwimmingPoolFavorite createFavorite() {
    return SwimmingPoolFavorite.builder()
        .targetId(1L)
        .targetType(SWIMMING_POOL)
        .imagePath("MOCK_IMAGE_PATH")
        .distance(500)
        .name("MOCK_NAME")
        .address("MOCK_ADDRESS")
        .build();
  }

}
