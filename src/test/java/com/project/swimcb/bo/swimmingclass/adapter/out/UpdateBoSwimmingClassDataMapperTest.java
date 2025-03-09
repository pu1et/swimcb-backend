package com.project.swimcb.bo.swimmingclass.adapter.out;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.instructor.domain.SwimmingInstructor;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Days;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.RegistrationCapacity;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Ticket;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Time;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassCommand.Type;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UpdateBoSwimmingClassDataMapperTest {

  @InjectMocks
  private UpdateBoSwimmingClassDataMapper mapper;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private Query query;

  @Mock
  private SwimmingClassTypeRepository swimmingClassTypeRepository;

  @Mock
  private SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Mock
  private SwimmingInstructorRepository instructorRepository;

  @BeforeEach
  void setUp() {
    when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
    when(entityManager.createQuery(anyString())).thenReturn(query);

    mapper = spy(new UpdateBoSwimmingClassDataMapper(entityManager, swimmingClassTypeRepository,
        swimmingClassSubTypeRepository, instructorRepository));
  }

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
    when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(existingInstructor));
    // when
    mapper.updateSwimmingClass(request);
    // then
    verify(swimmingClassTypeRepository, only()).findById(request.type().typeId());
    verify(swimmingClassSubTypeRepository, only()).findById(request.type().subTypeId());
    verify(instructorRepository, only()).findById(request.instructorId());

    verify(query, times(1)).executeUpdate();
  }

  private static class TestUpdateBoSwimmingClassCommandFactory {

    private static UpdateBoSwimmingClassCommand create() {
      return UpdateBoSwimmingClassCommand.builder()
          .swimmingPoolId(1L)
          .swimmingClassId(2L)
          .type(Type.builder()
              .typeId(3L)
              .subTypeId(4L)
              .build())
          .days(Days.builder()
              .isMonday(true)
              .isTuesday(false)
              .isWednesday(true)
              .isThursday(false)
              .isFriday(true)
              .isSaturday(false)
              .isSunday(true)
              .build())
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
          .isExposed(true)
          .build();
    }
  }

  private static class TestSwimmingTypeFactory {

    private static SwimmingClassType create() throws Exception {
      val swimmingClassTypeConstructor = SwimmingClassType.class.getDeclaredConstructor();
      swimmingClassTypeConstructor.setAccessible(true);
      val swimmingClassType = swimmingClassTypeConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingClassType, "id", 1L);
      return swimmingClassType;
    }
  }

  private static class TestSwimmingSubTypeFactory {

    private static SwimmingClassSubType create() throws Exception {
      val swimmingClassSubTypeConstructor = SwimmingClassSubType.class.getDeclaredConstructor();
      swimmingClassSubTypeConstructor.setAccessible(true);
      val swimmingClassSubType = swimmingClassSubTypeConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingClassSubType, "id", 1L);
      return swimmingClassSubType;
    }
  }

  private static class TestSwimmingInstructorFactory {

    private static SwimmingInstructor create() throws Exception {
      val swimmingInstructorConstructor = SwimmingInstructor.class.getDeclaredConstructor();
      swimmingInstructorConstructor.setAccessible(true);
      val swimmingClassInstructor = swimmingInstructorConstructor.newInstance();
      ReflectionTestUtils.setField(swimmingClassInstructor, "id", 1L);
      return swimmingClassInstructor;
    }
  }
}