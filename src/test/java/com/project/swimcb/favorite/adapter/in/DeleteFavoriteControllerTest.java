package com.project.swimcb.favorite.adapter.in;

import static com.project.swimcb.favorite.adapter.in.DeleteFavoriteControllerTest.MEMBER_ID;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.favorite.application.in.DeleteFavoriteUseCase;
import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = DeleteFavoriteController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class DeleteFavoriteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private DeleteFavoriteUseCase useCase;

  static final long MEMBER_ID = 1L;

  private final String PATH = "/api/favorites/{targetType}/{targetId}";
  private final FavoriteTargetType TARGET_TYPE = SWIMMING_POOL;
  private final long TARGET_ID = 1L;

  @Test
  @DisplayName("유효한 즐겨찾기 ID와 토큰 정보가 주어졌을 때 즐겨찾기 삭제가 성공해야 한다")
  void givenValidFavoriteIdAndToken_whenDeleteFavorite_thenShouldDeleteSuccessfully()
      throws Exception {
    // given
    // when
    // then
    mockMvc.perform(delete(PATH, TARGET_TYPE, TARGET_ID))
        .andExpect(status().isOk());

    then(useCase).should().deleteFavorite(assertArg(i -> {
          assertThat(i.memberId()).isEqualTo(MEMBER_ID);
          assertThat(i.targetType()).isEqualTo(TARGET_TYPE);
          assertThat(i.targetId()).isEqualTo(TARGET_ID);
        })
    );
  }

}
