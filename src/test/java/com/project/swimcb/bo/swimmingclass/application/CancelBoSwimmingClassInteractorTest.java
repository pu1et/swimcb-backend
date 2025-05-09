package com.project.swimcb.bo.swimmingclass.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.CancelBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
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
class CancelBoSwimmingClassInteractorTest {

  @InjectMocks
  private CancelBoSwimmingClassInteractor interactor;

  @Mock
  private SwimmingClassRepository swimmingClassRepository;

  @Mock
  private UpdateBoSwimmingClassDsGateway updateBoSwimmingClassDsGateway;

  @Nested
  @DisplayName("수영 클래스 취소 시")
  class CancelSwimmingClassTests {

    private CancelBoSwimmingClassCommand command;

    @BeforeEach
    void setUp() {
      val swimmingPoolId = 1L;
      val swimmingClassId = 2L;
      val cancelReason = "DUMMY_CANCEL_REASON";
      command = CancelBoSwimmingClassCommand.builder()
          .swimmingPoolId(swimmingPoolId)
          .swimmingClassId(swimmingClassId)
          .cancelReason(cancelReason)
          .build();
    }

    @Test
    @DisplayName("클래스가 존재하면 연관된 티켓을 삭제하고 클래스를 취소 상태로 변경한다")
    void shouldCancelClassAndDeleteTicketsWhenClassExists() {
      // given
      val swimmingClass = mock(SwimmingClass.class);
      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.of(swimmingClass));

      // when
      interactor.cancelBoSwimmingClass(command);

      // then
      verify(updateBoSwimmingClassDsGateway, only()).deleteAllTicketsBySwimmingClassId(
          command.swimmingClassId());
      verify(swimmingClassRepository, only()).findBySwimmingPool_IdAndId(command.swimmingPoolId(),
          command.swimmingClassId());
      verify(swimmingClass, only()).cancel(command.cancelReason());
    }

    @Test
    @DisplayName("클래스가 존재하지 않으면 NoSuchElementException을 던진다")
    void shouldThrowExceptionWhenClassDoesNotExist() {
      // given
      when(swimmingClassRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
          .thenReturn(Optional.empty());

      // when
      // then
      assertThatThrownBy(() -> interactor.cancelBoSwimmingClass(command))
          .isInstanceOf(NoSuchElementException.class);

      verify(updateBoSwimmingClassDsGateway, only()).deleteAllTicketsBySwimmingClassId(
          command.swimmingClassId());
    }

    @Test
    @DisplayName("파라미터가 null이 전달되면 NullPointerException을 던진다")
    void shouldThrowExceptionWhenArgumentIsNull() {
      // when
      // then
      assertThatThrownBy(() -> interactor.cancelBoSwimmingClass(null))
          .isInstanceOf(NullPointerException.class);
    }
  }
}
