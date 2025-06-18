package com.project.swimcb.bo.instructor.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.instructor.domain.CreateBoInstructorCommand;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.project.swimcb.db.entity.SwimmingPoolEntity;
import com.project.swimcb.db.repository.SwimmingPoolRepository;
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
class CreateBoInstructorInteractorTest {

  @InjectMocks
  private CreateBoInstructorInteractor interactor;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Mock
  private SwimmingInstructorRepository swimmingInstructorRepository;

  @Test
  @DisplayName("강사를 성공적으로 생성한다.")
  void shouldCreateInstructorSuccessfully() {
    // given
    val request = TestCreateBoInstructorCommandFactory.create();
    val swimmingPool = mock(SwimmingPoolEntity.class);

    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.of(swimmingPool));
    // when
    interactor.createBoInstructor(request);
    // then
    verify(swimmingPoolRepository, only()).findById(request.swimmingPoolId());
    verify(swimmingInstructorRepository, only()).save(assertArg(i -> {
      assertThat(i.getSwimmingPool()).isEqualTo(swimmingPool);
      assertThat(i.getName()).isEqualTo(request.name());
    }));
  }

  @Test
  @DisplayName("존재하지 않는 수영장 ID로 요청 시 NoSuchElementException가 발생한다.")
  void shouldThrowNoSuchElementExceptionWhenSwimmingPoolNotFound() {
    // given
    val request = TestCreateBoInstructorCommandFactory.create();

    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.createBoInstructor(request))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("수영장이 존재하지 않습니다.");
  }

  private static class TestCreateBoInstructorCommandFactory {

    private static CreateBoInstructorCommand create() {
      return new CreateBoInstructorCommand(1L, "DUMMY_NAME");
    }
  }
}
