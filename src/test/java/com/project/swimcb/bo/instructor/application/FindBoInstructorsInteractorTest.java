package com.project.swimcb.bo.instructor.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.instructor.adapter.in.FindBoInstructorsResponse.Instructor;
import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.project.swimcb.db.entity.SwimmingPoolEntity;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FindBoInstructorsInteractorTest {

  @InjectMocks
  private FindBoInstructorsInteractor interactor;

  @Mock
  private SwimmingInstructorRepository swimmingInstructorRepository;

  @Test
  @DisplayName("강사를 성공적으로 조회한다.")
  void shouldFindInstructorsSuccessfully() throws Exception {
    // given
    val swimmingPoolId = 1L;
    val instructors = TestInstructorFactory.create(swimmingPoolId);
    val expectedInstructorIds = instructors.stream().map(SwimmingInstructorEntity::getId).toList();
    val expectedInstructorNames = instructors.stream().map(SwimmingInstructorEntity::getName).toList();

    when(swimmingInstructorRepository.findAllBySwimmingPool_Id(anyLong())).thenReturn(instructors);
    // when
    val response = interactor.findBoInstructors(swimmingPoolId);
    // then
    assertThat(response.instructors()).hasSize(2);
    assertThat(response.instructors()).extracting(Instructor::instructorId)
        .containsExactlyElementsOf(expectedInstructorIds);
    assertThat(response.instructors()).extracting(Instructor::name)
        .containsExactlyElementsOf(expectedInstructorNames);

    verify(swimmingInstructorRepository, only()).findAllBySwimmingPool_Id(swimmingPoolId);
  }

  private static class TestInstructorFactory {

    private static List<SwimmingInstructorEntity> create(long swimmingPoolId) throws Exception {
      val swimmingPoolConstructor = SwimmingPoolEntity.class.getDeclaredConstructor();
      swimmingPoolConstructor.setAccessible(true);
      val swimmingPool = swimmingPoolConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingPool, "id", swimmingPoolId);

      val instructorConstructor = SwimmingInstructorEntity.class.getDeclaredConstructor();
      instructorConstructor.setAccessible(true);

      val instructor1 = instructorConstructor.newInstance();
      ReflectionTestUtils.setField(instructor1, "id", 1L);
      ReflectionTestUtils.setField(instructor1, "swimmingPool", swimmingPool);
      ReflectionTestUtils.setField(instructor1, "name", "DUMMY_NAME1");

      val instructor2 = instructorConstructor.newInstance();
      ReflectionTestUtils.setField(instructor2, "id", 2L);
      ReflectionTestUtils.setField(instructor2, "swimmingPool", swimmingPool);
      ReflectionTestUtils.setField(instructor2, "name", "DUMMY_NAME2");

      return List.of(instructor1, instructor2);
    }

  }
}
