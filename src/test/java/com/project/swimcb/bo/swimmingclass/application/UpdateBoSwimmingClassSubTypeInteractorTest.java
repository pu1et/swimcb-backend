package com.project.swimcb.bo.swimmingclass.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassSubTypeCommand;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UpdateBoSwimmingClassSubTypeInteractorTest {

  @InjectMocks
  private UpdateBoSwimmingClassSubTypeInteractor interactor;

  @Mock
  private SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Test
  @DisplayName("강습구분을 성공적으로 업데이트한다.")
  void shouldUpdateSwimmingClassSubTypeSuccessfully() {
    // given
    val command = TestUpdateBoSwimmingClassSubTypeCommandFactory.create();
    val subType = mock(SwimmingClassSubType.class);

    when(swimmingClassSubTypeRepository.findBySwimmingClassType_IdAndId(anyLong(), anyLong()))
        .thenReturn(Optional.of(subType));
    // when
    interactor.updateBoSwimmingClassSubType(command);
    // then
    verify(swimmingClassSubTypeRepository, only()).findBySwimmingClassType_IdAndId(
        command.swimmingClassTypeId(), command.swimmingClassSubTypeId());
    verify(subType, only()).updateName(command.name());
  }

  @Test
  @DisplayName("존재하지 않는 강습형태ID/강습구분ID로 요청 시 NoSuchElementException가 발생한다.")
  void shouldThrowNoSuchElementExceptionWhenSwimmingClassTypeOrSubTypeNotFound() {
    // given
    val command = TestUpdateBoSwimmingClassSubTypeCommandFactory.create();

    when(swimmingClassSubTypeRepository.findBySwimmingClassType_IdAndId(anyLong(), anyLong()))
        .thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.updateBoSwimmingClassSubType(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("강습구분을 찾을 수 없습니다.");
  }

  private static class TestUpdateBoSwimmingClassSubTypeCommandFactory {

    public static UpdateBoSwimmingClassSubTypeCommand create() {
      return UpdateBoSwimmingClassSubTypeCommand.builder()
          .swimmingPoolId(1L).swimmingClassTypeId(2L).swimmingClassSubTypeId(3L).name("DUMMY_NAME")
          .build();
    }
  }
}