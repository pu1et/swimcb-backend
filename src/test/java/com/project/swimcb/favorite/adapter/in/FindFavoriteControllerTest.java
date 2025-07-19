package com.project.swimcb.favorite.adapter.in;

import static com.project.swimcb.favorite.adapter.in.FindFavoriteControllerTest.MEMBER_ID;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.favorite.application.in.FindFavoriteUseCase;
import com.project.swimcb.favorite.domain.Favorite;
import com.project.swimcb.favorite.domain.FindFavoriteCondition;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindFavoriteController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindFavoriteControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private FindFavoriteUseCase useCase;

  @MockitoBean
  private FindFavoriteResponseMapper responseMapper;

  @Autowired
  private ObjectMapper objectMapper;

  static final long MEMBER_ID = 1L;

  private Page<Favorite> result;
  private FindFavoriteResponse mockResponse;

  @BeforeEach
  void setUp() {
    result = new PageImpl<>(List.of());
    mockResponse = new FindFavoriteResponse(new PageImpl<>(List.of()));
  }

  @Test
  @DisplayName("즐겨찾기 조회 - 성공")
  void shouldFindFavoritesSuccessfully() throws Exception {
    // given
    given(useCase.findFavorites(any(FindFavoriteCondition.class))).willReturn(result);
    given(responseMapper.mapToResponse(any())).willReturn(mockResponse);

    // when
    mockMvc.perform(get("/api/favorites")
            .param("memberLatitude", "37.5665")
            .param("memberLongitude", "126.9789")
            .param("targetType", "SWIMMING_POOL")
            .param("page", "1")
            .param("size", "10"))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));

    // then
    then(useCase).should().findFavorites(
        FindFavoriteCondition.builder()
            .memberId(MEMBER_ID)
            .memberLatitude(37.5665)
            .memberLongitude(126.9789)
            .targetType(SWIMMING_POOL)
            .pageable(PageRequest.of(0, 10))
            .build()
    );
  }

  @Test
  @DisplayName("즐겨찾기 조회 - targetType 없이 성공")
  void shouldFindFavoritesWithoutTargetType() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(get("/api/favorites")
            .param("memberLatitude", "37.5665")
            .param("memberLongitude", "126.9789")
            .param("page", "1")
            .param("size", "10"))
        .andExpect(status().isOk());
  }

  @Nested
  @DisplayName("파라미터 검증")
  class ParameterValidationTests {

    @Test
    @DisplayName("즐겨찾기 조회 - 필수 파라미터 누락시 실패 (memberLatitude)")
    void shouldFailWhenMissingMemberLatitude() throws Exception {
      // given
      // when
      // then
      mockMvc.perform(get("/api/favorites")
              .param("memberLongitude", "126.9789")
              .param("page", "1")
              .param("size", "10"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("즐겨찾기 조회 - 필수 파라미터 누락시 실패 (memberLongitude)")
    void shouldFailWhenMissingMemberLongitude() throws Exception {
      // given
      // when
      // then
      mockMvc.perform(get("/api/favorites")
              .param("memberLatitude", "37.5665")
              .param("page", "1")
              .param("size", "10"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("즐겨찾기 조회 - 필수 파라미터 누락시 실패 (page)")
    void shouldFailWhenMissingPage() throws Exception {
      // given
      // when
      // then
      mockMvc.perform(get("/api/favorites")
              .param("memberLatitude", "37.5665")
              .param("memberLongitude", "126.9789")
              .param("size", "10"))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("즐겨찾기 조회 - 필수 파라미터 누락시 실패 (size)")
    void shouldFailWhenMissingSize() throws Exception {
      // given
      // when
      // then
      mockMvc.perform(get("/api/favorites")
              .param("memberLatitude", "37.5665")
              .param("memberLongitude", "126.9789")
              .param("page", "1"))
          .andExpect(status().isBadRequest());
    }

  }

}
