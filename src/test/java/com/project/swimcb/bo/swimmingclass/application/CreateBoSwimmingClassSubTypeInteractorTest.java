package com.project.swimcb.bo.swimmingclass.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassSubTypeCommand;
import com.project.swimcb.db.entity.SwimmingClassTypeEntity;
import com.project.swimcb.db.repository.SwimmingClassSubTypeRepository;
import com.project.swimcb.db.repository.SwimmingClassTypeRepository;
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
class CreateBoSwimmingClassSubTypeInteractorTest {

  @InjectMocks
  private CreateBoSwimmingClassSubTypeInteractor interactor;

  @Mock
  private SwimmingClassTypeRepository swimmingClassTypeRepository;

  @Mock
  private SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Test
  @DisplayName("강습구분을 성공적으로 생성한다.")
  void shouldCreateSwimmingClassSubTypeSuccessfully() {
    // given
    val command = TestCreateBoSwimmingClassSubTypeCommandFactory.create();
    val classType = mock(SwimmingClassTypeEntity.class);

    when(swimmingClassTypeRepository.findById(command.swimmingClassTypeId()))
        .thenReturn(Optional.of(classType));
    // when
    interactor.createBoSwimmingClassSubType(command);
    // then
    verify(swimmingClassTypeRepository, only()).findById(command.swimmingClassTypeId());
    verify(swimmingClassSubTypeRepository, only()).save(assertArg(i -> {
      assertThat(i.getSwimmingClassType()).isEqualTo(classType);
      assertThat(i.getName()).isEqualTo(command.name());
    }));
  }

  @Test
  @DisplayName("존재하지 않는 강습형태 ID로 요청 시 NoSuchElementException가 발생한다.")
  void shouldThrowNoSuchElementExceptionWhenSwimmingClassTypeNotFound() {
    // given
    val command = TestCreateBoSwimmingClassSubTypeCommandFactory.create();

    when(swimmingClassTypeRepository.findById(command.swimmingClassTypeId()))
        .thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.createBoSwimmingClassSubType(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("강습형태를 찾을 수 없습니다.");
  }

  private static class TestCreateBoSwimmingClassSubTypeCommandFactory {

    private static CreateBoSwimmingClassSubTypeCommand create() {
      return CreateBoSwimmingClassSubTypeCommand.builder()
          .swimmingPoolId(1L).swimmingClassTypeId(2L).name("DUMMY_NAME").build();
    }
  }
}
