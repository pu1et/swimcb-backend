package com.project.swimcb.bo.instructor.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.instructor.domain.DeleteBoInstructorCommand;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeleteBoInstructorInteractorTest {

  @InjectMocks
  private DeleteBoInstructorInteractor interactor;

  @Mock
  private SwimmingInstructorRepository swimmingInstructorRepository;

  @Test
  @DisplayName("강사를 성공적으로 삭제한다.")
  void shouldDeleteInstructorSuccessfully() {
    // given
    val command = TestDeleteBoInstructorCommandFactory.create();

    when(swimmingInstructorRepository.deleteBySwimmingPool_IdAndId(anyLong(), anyLong()))
        .thenReturn(1);
    // when
    interactor.deleteBoInstructor(command);
    // then
    verify(swimmingInstructorRepository, only()).deleteBySwimmingPool_IdAndId(
        command.swimmingPoolId(), command.instructorId());

  }

  @Test
  @DisplayName("존재하지 않는 강습형태ID/강습구분ID로 요청 시 NoSuchElementException가 발생한다.")
  void shouldThrowNoSuchElementExceptionWhenSwimmingClassTypeOrSubTypeNotFound() {
    // given
    val command = TestDeleteBoInstructorCommandFactory.create();

    when(swimmingInstructorRepository.deleteBySwimmingPool_IdAndId(anyLong(), anyLong()))
        .thenReturn(0);
    // when
    // then
    assertThatThrownBy(() -> interactor.deleteBoInstructor(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("강사가 존재하지 않습니다.");
  }

  private static class TestDeleteBoInstructorCommandFactory {

    private static DeleteBoInstructorCommand create() {
      return DeleteBoInstructorCommand.builder().swimmingPoolId(1L).instructorId(2L).build();
    }
  }
}
