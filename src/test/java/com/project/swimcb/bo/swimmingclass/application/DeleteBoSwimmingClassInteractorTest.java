package com.project.swimcb.bo.swimmingclass.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicketRepository;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteBoSwimmingClassInteractorTest {

  @InjectMocks
  private DeleteBoSwimmingClassInteractor interactor;

  @Mock
  private SwimmingClassRepository swimmingClassRepository;

  @Mock
  private SwimmingClassTicketRepository swimmingClassTicketRepository;

  @Test
  @DisplayName("클래스를 성공적으로 삭제한다.")
  void shouldDeleteSwimmingClassSuccessfully() {
    // given
    val request = DeleteBoSwimmingClassCommandFactory.create();

    when(swimmingClassRepository.deleteBySwimmingPool_IdAndId(anyLong(), anyLong())).thenReturn(1);
    // when
    interactor.deleteBoSwimmingClass(request);
    // then
    verify(swimmingClassTicketRepository, only()).deleteBySwimmingClass_Id(
        request.swimmingClassId());
    verify(swimmingClassRepository, only()).deleteBySwimmingPool_IdAndId(request.swimmingPoolId(),
        request.swimmingClassId());
  }

  @Test
  @DisplayName("존재하지 않는 클래스 ID로 요청 시 NoSuchElementException가 발생한다.")
  void shouldThrowNoSuchElementExceptionWhenSwimmingClassNotFound() {
    // given
    val request = DeleteBoSwimmingClassCommandFactory.create();

    when(swimmingClassRepository.deleteBySwimmingPool_IdAndId(anyLong(), anyLong())).thenReturn(0);
    // when
    // then
    assertThatThrownBy(() -> interactor.deleteBoSwimmingClass(request))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("클래스가 존재하지 않습니다.");
  }

  private static class DeleteBoSwimmingClassCommandFactory {

    private static DeleteBoSwimmingClassCommand create() {
      return DeleteBoSwimmingClassCommand.builder()
          .swimmingPoolId(1L)
          .swimmingClassId(1L)
          .build();
    }
  }
}