package com.project.swimcb.swimming_pool_review.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimming_pool_review.domain.SwimmingPoolReviewRepository;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindTotalReviewCountInteractorTest {

  @InjectMocks
  private FindTotalReviewCountInteractor findTotalReviewCountInteractor;

  @Mock
  private SwimmingPoolReviewRepository reviewRepository;

  @Test
  @DisplayName("특정 수영장의 리뷰 개수 조회 - 리뷰가 있는 경우")
  void shouldReturnCorrectCountWhenReviewExists() {
    // given
    val swimmingPoolId = 1L;
    val expectedCount = 10;

    when(reviewRepository.countBySwimmingPool_Id(swimmingPoolId)).thenReturn(expectedCount);
    // when
    val result = findTotalReviewCountInteractor.findTotalReviewCount(swimmingPoolId);
    // then
    verify(reviewRepository, only()).countBySwimmingPool_Id(swimmingPoolId);

    assertThat(result).isEqualTo(expectedCount);
  }

  @Test
  @DisplayName("특정 수영장의 리뷰 개수 조회 - 리뷰가 없는 경우")
  void shouldReturnCorrectCountWhenReviewDoesNotExist() {
    // given
    val swimmingPoolId = 1L;
    val expectedCount = 0;

    when(reviewRepository.countBySwimmingPool_Id(swimmingPoolId)).thenReturn(expectedCount);
    // when
    val result = findTotalReviewCountInteractor.findTotalReviewCount(swimmingPoolId);
    // then
    verify(reviewRepository, only()).countBySwimmingPool_Id(swimmingPoolId);

    assertThat(result).isEqualTo(expectedCount);
  }

  @Test
  @DisplayName("존재하지 않는 수영장 ID로 조회 시 리뷰 개수는 0이다.")
  void shouldReturnZeroWhenSwimmingPoolDoesNotExist() {
    // given
    val nonExistentSwimmingPoolId = 999L;

    when(reviewRepository.countBySwimmingPool_Id(nonExistentSwimmingPoolId)).thenReturn(0);
    // when
    val result = findTotalReviewCountInteractor.findTotalReviewCount(nonExistentSwimmingPoolId);
    // then
    verify(reviewRepository, only()).countBySwimmingPool_Id(nonExistentSwimmingPoolId);

    assertThat(result).isZero();
  }
}