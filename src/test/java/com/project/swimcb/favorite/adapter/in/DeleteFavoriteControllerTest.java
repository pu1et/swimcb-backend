package com.project.swimcb.favorite.adapter.in;

import static com.project.swimcb.favorite.adapter.in.DeleteFavoriteControllerTest.MEMBER_ID;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.favorite.application.in.DeleteFavoriteUseCase;
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

  private final String PATH = "/api/favorites/1";
  private final long FAVORITE_ID = 1L;

  @Test
  @DisplayName("유효한 즐겨찾기 ID와 토큰 정보가 주어졌을 때 즐겨찾기 삭제가 성공해야 한다")
  void givenValidFavoriteIdAndToken_whenDeleteFavorite_thenShouldDeleteSuccessfully()
      throws Exception {
    // given
    // when
    // then
    mockMvc.perform(delete(PATH, FAVORITE_ID))
        .andExpect(status().isOk());

    then(useCase).should().deleteFavorite(MEMBER_ID, FAVORITE_ID);
  }

}
