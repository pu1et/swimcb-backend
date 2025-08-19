package com.project.swimcb.main.adapter.in;

import static com.project.swimcb.main.adapter.in.FindRecommendationSwimmingPoolControllerTest.MEMBER_ID;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.main.application.port.in.FindRecommendationSwimmingPoolUseCase;
import com.project.swimcb.main.domain.FindRecommendationSwimmingPoolCondition;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindRecommendationSwimmingPoolController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindRecommendationSwimmingPoolControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindRecommendationSwimmingPoolUseCase useCase;

  @MockitoBean
  private FindRecommendationSwimmingPoolResponseMapper responseMapper;

  static final long MEMBER_ID = 1L;

  private final String PATH = "/api/main/recommendation";

  @Nested
  @DisplayName("추천 수영장 조회")
  class FindRecommendationSwimmingPool {

    @Test
    @DisplayName("성공 - 유효한 위도, 경도로 추천 수영장을 조회한다")
    void success() throws Exception {
      // given
      val memberLatitude = 37.5665;
      val memberLongitude = 126.9780;

      val recommendationPools = List.of(
          TestRecommendationSwimmingPoolFactory.create()
      );

      val response = TestFindRecommendationSwimmingPoolResponseFactory.create();

      given(useCase.findRecommendationSwimmingPools(any())).willReturn(recommendationPools);
      given(responseMapper.toResponse(anyList())).willReturn(response);

      // when 
      // then
      mockMvc.perform(get(PATH)
              .param("member-latitude", String.valueOf(memberLatitude))
              .param("member-longitude", String.valueOf(memberLongitude)))
          .andExpect(status().isOk())
          .andExpect(content().string(containsString(objectMapper.writeValueAsString(response))));

      then(useCase).should().findRecommendationSwimmingPools(
          FindRecommendationSwimmingPoolCondition.builder()
              .memberId(MEMBER_ID)
              .memberLatitude(memberLatitude)
              .memberLongitude(memberLongitude)
              .build()
      );

      then(responseMapper).should().toResponse(recommendationPools);
    }

    @Test
    @DisplayName("실패 - member-latitude 파라미터가 누락된 경우")
    void failWhenMemberLatitudeMissing() throws Exception {
      // given
      val memberLongitude = 126.9780;

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("member-longitude", String.valueOf(memberLongitude)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("실패 - member-longitude 파라미터가 누락된 경우")
    void failWhenMemberLongitudeMissing() throws Exception {
      // given
      val memberLatitude = 37.5665;

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("member-latitude", String.valueOf(memberLatitude)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("실패 - 잘못된 형식의 위도 값")
    void failWhenInvalidLatitudeFormat() throws Exception {
      // given
      val invalidLatitude = "invalid";
      val memberLongitude = 126.9780;

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("member-latitude", invalidLatitude)
              .param("member-longitude", String.valueOf(memberLongitude)))
          .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("실패 - 잘못된 형식의 경도 값")
    void failWhenInvalidLongitudeFormat() throws Exception {
      // given
      val memberLatitude = 37.5665;
      val invalidLongitude = "invalid";

      // when
      // then
      mockMvc.perform(get(PATH)
              .param("member-latitude", String.valueOf(memberLatitude))
              .param("member-longitude", invalidLongitude))
          .andExpect(status().isBadRequest());
    }

  }

  private static class TestRecommendationSwimmingPoolFactory {

    public static RecommendationSwimmingPool create() {
      return new RecommendationSwimmingPool(
          1L,
          "https://example.com/image.jpg",
          100L,
          500,
          "테스트 수영장",
          "서울시 강남구 테스트로 123",
          4.5,
          150
      );
    }

  }

  private static class TestFindRecommendationSwimmingPoolResponseFactory {

    private static FindRecommendationSwimmingPoolResponse create() {
      return FindRecommendationSwimmingPoolResponse.builder()
          .swimmingPools(List.of(
              FindRecommendationSwimmingPoolResponse.SwimmingPool.builder()
                  .swimmingPoolId(1L)
                  .imageUrl("https://example.com/image.jpg")
                  .favoriteId(100L)
                  .distance(500)
                  .name("테스트 수영장")
                  .address("서울시 강남구 테스트로 123")
                  .rating(4.5)
                  .reviewCount(150)
                  .build()
          ))
          .build();
    }

  }

}
