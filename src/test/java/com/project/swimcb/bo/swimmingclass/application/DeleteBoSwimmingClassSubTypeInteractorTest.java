package com.project.swimcb.bo.swimmingclass.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassSubTypeCommand;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteBoSwimmingClassSubTypeInteractorTest {

  @InjectMocks
  private DeleteBoSwimmingClassSubTypeInteractor interactor;

  @Mock
  private SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Test
  @DisplayName("강습구분을 성공적으로 삭제한다.")
  void shouldDeleteSwimmingClassSubTypeSuccessfully() {
    // given
    val command = TestDeleteBoSwimmingClassSubTypeCommandFactory.create();

    when(swimmingClassSubTypeRepository.deleteBySwimmingClassType_IdAndId(anyLong(), anyLong()))
        .thenReturn(1);
    // when
    interactor.deleteBoSwimmingClassSubType(command);
    // then
    verify(swimmingClassSubTypeRepository, only()).deleteBySwimmingClassType_IdAndId(
        command.swimmingClassTypeId(), command.swimmingClassSubTypeId());

  }

  @Test
  @DisplayName("존재하지 않는 강습형태ID/강습구분ID로 요청 시 NoSuchElementException가 발생한다.")
  void shouldThrowNoSuchElementExceptionWhenSwimmingClassTypeOrSubTypeNotFound() {
    // given
    val command = TestDeleteBoSwimmingClassSubTypeCommandFactory.create();

    when(swimmingClassSubTypeRepository.deleteBySwimmingClassType_IdAndId(anyLong(), anyLong()))
        .thenReturn(0);
    // when
    // then
    assertThatThrownBy(() -> interactor.deleteBoSwimmingClassSubType(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("강습구분을 찾을 수 없습니다.");
  }

  private static class TestDeleteBoSwimmingClassSubTypeCommandFactory {

    private static DeleteBoSwimmingClassSubTypeCommand create() {
      return DeleteBoSwimmingClassSubTypeCommand.builder()
          .swimmingPoolId(1L).swimmingClassTypeId(2L).swimmingClassSubTypeId(3L).build();
    }
  }
}