package com.project.swimcb.bo.freeswimming.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.project.swimcb.bo.freeswimming.domain.CancelBoFreeSwimmingCommand;
import com.project.swimcb.db.entity.FreeSwimmingEntity;
import com.project.swimcb.db.repository.FreeSwimmingRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CancelBoFreeSwimmingInteractorTest {

  @InjectMocks
  private CancelBoFreeSwimmingInteractor interactor;

  @Mock
  private FreeSwimmingRepository freeSwimmingRepository;

  private FreeSwimmingEntity freeSwimming;

  @BeforeEach
  void setUp() {
    freeSwimming = mock(FreeSwimmingEntity.class);
  }

  @Nested
  @DisplayName("자유수영 취소 시")
  class DescribeCancelBoFreeSwimming {

    @Nested
    @DisplayName("자유수영이 존재하는 경우")
    class ContextWithExistingFreeSwimming {

      @Test
      @DisplayName("취소 처리에 성공한다")
      void shouldCancelSuccessfully() {
        // given
        val command = CancelBoFreeSwimmingCommand.builder()
            .swimmingPoolId(1L)
            .freeSwimmingId(1L)
            .cancelReason("강사 개인사유")
            .build();

        given(freeSwimmingRepository.findBySwimmingPoolIdAndId(command.swimmingPoolId(),
            command.freeSwimmingId()))
            .willReturn(Optional.of(freeSwimming));

        // when
        interactor.cancelBoFreeSwimming(command);

        // then
        then(freeSwimmingRepository).should()
            .findBySwimmingPoolIdAndId(command.swimmingPoolId(), command.freeSwimmingId());
        then(freeSwimming).should().cancel(command.cancelReason());
      }

    }

    @Nested
    @DisplayName("자유수영이 존재하지 않는 경우")
    class ContextWithNonExistentFreeSwimming {

      @Test
      @DisplayName("예외를 발생시킨다")
      void shouldThrowExceptionWhenFreeSwimmingNotFound() {
        // given
        val command = CancelBoFreeSwimmingCommand.builder()
            .swimmingPoolId(1L)
            .freeSwimmingId(999L)
            .cancelReason("강사 개인사유")
            .build();

        given(freeSwimmingRepository.findBySwimmingPoolIdAndId(command.swimmingPoolId(),
            command.freeSwimmingId()))
            .willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> interactor.cancelBoFreeSwimming(command))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("자유수영이 존재하지 않습니다.");

        then(freeSwimming).shouldHaveNoInteractions();
      }

    }

  }

}
