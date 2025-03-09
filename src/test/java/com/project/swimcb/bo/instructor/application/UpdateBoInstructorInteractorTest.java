package com.project.swimcb.bo.instructor.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.instructor.domain.SwimmingInstructor;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import com.project.swimcb.bo.instructor.domain.UpdateBoInstructorCommand;
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
class UpdateBoInstructorInteractorTest {

  @InjectMocks
  private UpdateBoInstructorInteractor interactor;

  @Mock
  private SwimmingInstructorRepository swimmingInstructorRepository;

  @Test
  @DisplayName("강사를 성공적으로 업데이트한다.")
  void shouldUpdateInstructorSuccessfully() {
    // given
    val command = TestUpdateBoInstructorCommandFactory.create();
    val instructor = mock(SwimmingInstructor.class);

    when(swimmingInstructorRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
        .thenReturn(Optional.of(instructor));
    // when
    interactor.updateBoInstructor(command);
    // then
    verify(swimmingInstructorRepository, only()).findBySwimmingPool_IdAndId(
        command.swimmingPoolId(), command.instructorId());
    verify(instructor, only()).updateName(command.name());
  }

  @Test
  @DisplayName("존재하지 않는 수영장ID/강사ID로 요청 시 NoSuchElementException가 발생한다.")
  void shouldThrowNoSuchElementExceptionWhenSwimmingPoolOrInstructorNotFound() {
    // given
    val command = TestUpdateBoInstructorCommandFactory.create();

    when(swimmingInstructorRepository.findBySwimmingPool_IdAndId(anyLong(), anyLong()))
        .thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.updateBoInstructor(command))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("강사가 존재하지 않습니다.");
  }

  private static class TestUpdateBoInstructorCommandFactory {

    private static UpdateBoInstructorCommand create() {
      return UpdateBoInstructorCommand.builder()
          .swimmingPoolId(1L).instructorId(2L).name("DUMMY_NAME")
          .build();
    }
  }
}