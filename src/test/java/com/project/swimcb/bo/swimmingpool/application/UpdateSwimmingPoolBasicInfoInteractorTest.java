package com.project.swimcb.bo.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolImage;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolImageRepository;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolRepository;
import com.project.swimcb.bo.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateSwimmingPoolBasicInfoInteractorTest {

  @InjectMocks
  private UpdateSwimmingPoolBasicInfoInteractor interactor;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Mock
  private SwimmingPoolImageRepository swimmingPoolImageRepository;

  @Test
  @DisplayName("유효한 수영장 ID로 기본 정보와 이미지를 업데이트 하면 성공한다.")
  void shouldUpdateBasicInfoAndImagesWhenSwimmingPoolIdIsValid() {
    // given
    val swimmingPoolId = 1L;
    val existingSwimmingPool = mock(SwimmingPool.class);
    val request = UpdateSwimmingPoolBasicInfoCommand.builder()
        .name("DUMMY_NAME")
        .address("DUMMY_ADDRESS")
        .imagePaths(List.of("DUMMY_IMAGE_PATH1", "DUMMY_IMAGE_PATH2"))
        .accountNo(AccountNo.of("DUMMY_ACCOUNT_NO"))
        .build();

    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.of(existingSwimmingPool));
    when(existingSwimmingPool.getId()).thenReturn(swimmingPoolId);
    // when
    interactor.updateBasicInfo(swimmingPoolId, request);
    // then
    val inOrder = inOrder(swimmingPoolRepository, existingSwimmingPool,
        swimmingPoolImageRepository);

    inOrder.verify(swimmingPoolRepository, times(1)).findById(swimmingPoolId);

    inOrder.verify(existingSwimmingPool, times(1)).updateBasicInfo(request);

    inOrder.verify(swimmingPoolImageRepository, times(1)).deleteAllBySwimmingPoolId(swimmingPoolId);

    inOrder.verify(swimmingPoolImageRepository, times(1)).saveAll(assertArg(i -> {
      assertThat(i).hasSize(2);
      assertThat(i).extracting(SwimmingPoolImage::getPath)
          .containsExactly("DUMMY_IMAGE_PATH1", "DUMMY_IMAGE_PATH2");
      assertThat(i).extracting(SwimmingPoolImage::getSwimmingPool)
          .containsOnly(existingSwimmingPool);
    }));
  }

  @Test
  @DisplayName("업데이트할 이미지가 없으면 이미지를 삭제만하고 저장하지 않는다.")
  void shouldDeleteImagesWhenImagesIsEmpty() {
    // given
    val swimmingPoolId = 1L;
    val existingSwimmingPool = mock(SwimmingPool.class);
    val request = UpdateSwimmingPoolBasicInfoCommand.builder()
        .name("DUMMY_NAME")
        .address("DUMMY_ADDRESS")
        .imagePaths(List.of())
        .accountNo(AccountNo.of("DUMMY_ACCOUNT_NO"))
        .build();

    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.of(existingSwimmingPool));
    when(existingSwimmingPool.getId()).thenReturn(swimmingPoolId);
    // when
    interactor.updateBasicInfo(swimmingPoolId, request);
    // then
    val inOrder = inOrder(swimmingPoolRepository, existingSwimmingPool,
        swimmingPoolImageRepository);

    inOrder.verify(swimmingPoolRepository, times(1)).findById(swimmingPoolId);

    inOrder.verify(existingSwimmingPool, times(1)).updateBasicInfo(request);

    inOrder.verify(swimmingPoolImageRepository, times(1)).deleteAllBySwimmingPoolId(swimmingPoolId);

    inOrder.verify(swimmingPoolImageRepository, never()).saveAll(any());
  }

  @Test
  @DisplayName("존재하지 않는 수영장 ID로 업데이트하면 IllegalArgumentException을 던진다.")
  void shouldThrowIllegalArgumentExceptionWhenSwimmingPoolIdIsNotExist() {
    // given
    val nonExistentSwimmingPoolId = 1L;
    val request = UpdateSwimmingPoolBasicInfoCommand.builder()
        .name("DUMMY_NAME")
        .address("DUMMY_ADDRESS")
        .imagePaths(List.of("DUMMY_IMAGE_PATH1", "DUMMY_IMAGE_PATH2"))
        .accountNo(AccountNo.of("DUMMY_ACCOUNT_NO"))
        .build();

    when(swimmingPoolRepository.findById(nonExistentSwimmingPoolId)).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.updateBasicInfo(nonExistentSwimmingPoolId, request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("수영장이 존재하지 않습니다.");
  }
}