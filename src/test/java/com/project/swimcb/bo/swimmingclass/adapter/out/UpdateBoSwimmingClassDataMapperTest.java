package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.db.entity.QSwimmingClassEntity.swimmingClassEntity;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Ticket;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Time;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Type;
import com.project.swimcb.db.entity.SwimmingClassSubTypeEntity;
import com.project.swimcb.db.entity.SwimmingClassTypeEntity;
import com.project.swimcb.db.entity.SwimmingInstructorEntity;
import com.project.swimcb.db.repository.SwimmingClassSubTypeRepository;
import com.project.swimcb.db.repository.SwimmingClassTypeRepository;
import com.project.swimcb.db.repository.SwimmingInstructorRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UpdateBoSwimmingClassDataMapperTest {

  private UpdateBoSwimmingClassDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  @Mock
  private SwimmingClassTypeRepository swimmingClassTypeRepository;

  @Mock
  private SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Mock
  private SwimmingInstructorRepository instructorRepository;

  @Mock
  private JPAUpdateClause updateClause;

  @Mock
  private EntityManager entityManager;

  @BeforeEach
  void setUp() {
    lenient().when(queryFactory.update(any())).thenReturn(updateClause);
    lenient().when(queryFactory.update(any())).thenReturn(updateClause);
    lenient().when(updateClause.set(any(), nullable(Object.class))).thenReturn(updateClause);
    lenient().when(updateClause.where(any(Predicate[].class))).thenReturn(updateClause);

    mapper = spy(new UpdateBoSwimmingClassDataMapper(queryFactory, swimmingClassTypeRepository,
        swimmingClassSubTypeRepository, instructorRepository, entityManager));
  }

  @Nested
  @DisplayName("수영 클래스 업데이트시")
  class UpdateSwimmingClass {

    @Nested
    @DisplayName("업데이트 대상 클래스가 존재하면")
    class WhenClassExists {

      @Test
      @DisplayName("수영 클래스를 성공적으로 업데이트한다.")
      void shouldUpdateSwimmingClassSuccessfully() throws Exception {
        // given
        val request = TestUpdateBoSwimmingClassCommandFactory.create();
        val existingClassType = TestSwimmingTypeFactory.create();
        val existingClassSubType = TestSwimmingSubTypeFactory.create();
        val existingInstructor = TestSwimmingInstructorFactory.create();

        when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassType));
        when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassSubType));
        when(instructorRepository.findById(any(Long.class))).thenReturn(
            Optional.of(existingInstructor));
        when(updateClause.execute()).thenReturn(1L);
        // when
        mapper.updateSwimmingClass(request);
        // then
        verify(swimmingClassTypeRepository, only()).findById(request.type().typeId());
        verify(swimmingClassSubTypeRepository, only()).findById(request.type().subTypeId());
        verify(instructorRepository, only()).findById(request.instructorId());

        verify(updateClause, times(1)).set(swimmingClassEntity.type, existingClassType);
        verify(updateClause, times(1)).set(swimmingClassEntity.subType, existingClassSubType);
        verify(updateClause, times(1)).set(swimmingClassEntity.daysOfWeek, 84);
        verify(updateClause, times(1)).set(swimmingClassEntity.startTime,
            request.time().startTime());
        verify(updateClause, times(1)).set(swimmingClassEntity.endTime, request.time().endTime());
        verify(updateClause, times(1)).set(swimmingClassEntity.instructor, existingInstructor);
        verify(updateClause, times(1)).set(swimmingClassEntity.totalCapacity,
            request.registrationCapacity().totalCapacity());
        verify(updateClause, times(1)).set(swimmingClassEntity.reservationLimitCount,
            request.registrationCapacity().reservationLimitCount());
        verify(updateClause, times(1)).set(swimmingClassEntity.isVisible, request.isExposed());
        verify(updateClause, times(1)).execute();
      }

    }

    @Nested
    @DisplayName("업데이트할 강사가 null이더라도")
    class WhenClassInstructorIsNull {

      @Test
      @DisplayName("수영 클래스를 성공적으로 업데이트한다.")
      void shouldThrowNoSuchElementExceptionWhenClassInstructorNotFound() throws Exception {
        // given
        val request = TestUpdateBoSwimmingClassCommandFactory.createWithoutInstructor();
        val existingClassType = TestSwimmingTypeFactory.create();
        val existingClassSubType = TestSwimmingSubTypeFactory.create();

        when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassType));
        when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassSubType));
        when(updateClause.execute()).thenReturn(1L);
        // when
        mapper.updateSwimmingClass(request);
        // then
        verify(instructorRepository, never()).findById(any(Long.class));
        verify(updateClause, times(1)).execute();
      }

    }

    @Nested
    @DisplayName("업데이트 대상 클래스가 존재하지 않으면")
    class WhenClassDoesNotExists {

      @Test
      @DisplayName("NoSuchElementException가 발생한다.")
      void shouldThrowNoSuchElementExceptionWhenSwimmingClassNotFound() throws Exception {
        // given
        val request = TestUpdateBoSwimmingClassCommandFactory.create();
        val existingClassType = TestSwimmingTypeFactory.create();
        val existingClassSubType = TestSwimmingSubTypeFactory.create();
        val existingInstructor = TestSwimmingInstructorFactory.create();

        when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassType));
        when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassSubType));
        when(instructorRepository.findById(any(Long.class))).thenReturn(
            Optional.of(existingInstructor));
        when(updateClause.execute()).thenReturn(0L);
        // when
        // then
        assertThatThrownBy(() -> mapper.updateSwimmingClass(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("클래스가 존재하지 않습니다.");

        verify(updateClause, never()).where(any());
      }

    }

    @Nested
    @DisplayName("업데이트할 강습형태가 존재하지 않으면")
    class WhenClassTypeDoesNotExists {

      @Test
      @DisplayName("NoSuchElementException가 발생한다.")
      void shouldThrowNoSuchElementExceptionWhenClassTypeNotFound() {
        // given
        val request = TestUpdateBoSwimmingClassCommandFactory.create();

        when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
            Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> mapper.updateSwimmingClass(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("강습형태가 존재하지 않습니다.");
      }

    }

    @Nested
    @DisplayName("업데이트할 강습구분이 존재하지 않으면")
    class WhenClassSubTypeDoesNotExists {

      @Test
      @DisplayName("NoSuchElementException가 발생한다.")
      void shouldThrowNoSuchElementExceptionWhenClassSubTypeNotFound() throws Exception {
        // given
        val request = TestUpdateBoSwimmingClassCommandFactory.create();
        val existingClassType = TestSwimmingTypeFactory.create();

        when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassType));
        when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(
            Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> mapper.updateSwimmingClass(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("강습구분이 존재하지 않습니다.");
      }

    }

    @Nested
    @DisplayName("업데이트할 강사가 조회시 존재하지 않으면")
    class WhenClassInstructorDoesNotExists {

      @Test
      @DisplayName("NoSuchElementException가 발생한다.")
      void shouldThrowNoSuchElementExceptionWhenClassInstructorNotFound() throws Exception {
        // given
        val request = TestUpdateBoSwimmingClassCommandFactory.create();
        val existingClassType = TestSwimmingTypeFactory.create();
        val existingClassSubType = TestSwimmingSubTypeFactory.create();

        when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassType));
        when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(
            Optional.of(existingClassSubType));
        when(instructorRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        // when
        // then
        assertThatThrownBy(() -> mapper.updateSwimmingClass(request))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("강사가 존재하지 않습니다.");
      }

    }

  }

  private static class TestUpdateBoSwimmingClassCommandFactory {

    private static UpdateBoSwimmingClassCommand create() {
      return common()
          .instructorId(5L)
          .build();
    }

    private static UpdateBoSwimmingClassCommand createWithoutInstructor() {
      return common()
          .instructorId(null)
          .build();
    }

    private static UpdateBoSwimmingClassCommand.UpdateBoSwimmingClassCommandBuilder common() {
      return UpdateBoSwimmingClassCommand.builder()
          .swimmingPoolId(1L)
          .swimmingClassId(2L)
          .type(Type.builder()
              .typeId(3L)
              .subTypeId(4L)
              .build())
          .days(List.of(MONDAY, WEDNESDAY, FRIDAY))
          .time(Time.builder()
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .build())
          .instructorId(5L)
          .tickets(List.of(
              Ticket.builder()
                  .name("DUMMY_TICKET_NAME1")
                  .price(10000)
                  .build(),
              Ticket.builder()
                  .name("DUMMY_TICKET_NAME2")
                  .price(20000)
                  .build()
          ))
          .registrationCapacity(RegistrationCapacity.builder()
              .totalCapacity(10)
              .reservationLimitCount(5)
              .build())
          .isExposed(true);
    }

  }

  private static class TestSwimmingTypeFactory {

    private static SwimmingClassTypeEntity create() throws Exception {
      val swimmingClassTypeConstructor = SwimmingClassTypeEntity.class.getDeclaredConstructor();
      swimmingClassTypeConstructor.setAccessible(true);
      val swimmingClassType = swimmingClassTypeConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingClassType, "id", 1L);
      return swimmingClassType;
    }

  }

  private static class TestSwimmingSubTypeFactory {

    private static SwimmingClassSubTypeEntity create() throws Exception {
      val swimmingClassSubTypeConstructor = SwimmingClassSubTypeEntity.class.getDeclaredConstructor();
      swimmingClassSubTypeConstructor.setAccessible(true);
      val swimmingClassSubType = swimmingClassSubTypeConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingClassSubType, "id", 1L);
      return swimmingClassSubType;
    }

  }

  private static class TestSwimmingInstructorFactory {

    private static SwimmingInstructorEntity create() throws Exception {
      val swimmingInstructorConstructor = SwimmingInstructorEntity.class.getDeclaredConstructor();
      swimmingInstructorConstructor.setAccessible(true);
      val swimmingClassInstructor = swimmingInstructorConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingClassInstructor, "id", 1L);
      return swimmingClassInstructor;
    }

  }

}
