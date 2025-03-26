package com.project.swimcb.bo.swimmingclass.application;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.instructor.domain.SwimmingInstructor;
import com.project.swimcb.bo.instructor.domain.SwimmingInstructorRepository;
import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassCommand.Ticket;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicket;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTicketRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTypeRepository;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPoolRepository;
import java.time.LocalDate;
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
class CreateBoSwimmingClassInteractorTest {

  @InjectMocks
  private CreateBoSwimmingClassInteractor interactor;

  @Mock
  private SwimmingPoolRepository swimmingPoolRepository;

  @Mock
  private SwimmingClassRepository swimmingClassRepository;

  @Mock
  private SwimmingClassTypeRepository swimmingClassTypeRepository;

  @Mock
  private SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Mock
  private SwimmingInstructorRepository instructorRepository;

  @Mock
  private SwimmingClassTicketRepository swimmingClassTicketRepository;

  private CreateBoSwimmingClassCommand command;
  private SwimmingPool swimmingPool;
  private SwimmingClassType swimmingClassType;
  private SwimmingClassSubType swimmingClassSubType;
  private SwimmingInstructor swimmingInstructor;
  private SwimmingClass savedSwimmingClass;

  @BeforeEach
  void setUp() throws Exception {
    command = CreateBoSwimmingClassCommandFactory.create();

    val swimmingPoolConstructor = SwimmingPool.class.getDeclaredConstructor();
    swimmingPoolConstructor.setAccessible(true);
    swimmingPool = swimmingPoolConstructor.newInstance();
    ReflectionTestUtils.setField(swimmingPool, "id", 1L);

    val swimmingClassTypeConstructor = SwimmingClassType.class.getDeclaredConstructor();
    swimmingClassTypeConstructor.setAccessible(true);
    swimmingClassType = swimmingClassTypeConstructor.newInstance();
    ReflectionTestUtils.setField(swimmingClassType, "id", 1L);

    val swimmingClassSubTypeConstructor = SwimmingClassSubType.class.getDeclaredConstructor();
    swimmingClassSubTypeConstructor.setAccessible(true);
    swimmingClassSubType = swimmingClassSubTypeConstructor.newInstance();
    ReflectionTestUtils.setField(swimmingClassSubType, "id", 1L);

    val swimmingInstructorConstructor = SwimmingInstructor.class.getDeclaredConstructor();
    swimmingInstructorConstructor.setAccessible(true);
    swimmingInstructor = swimmingInstructorConstructor.newInstance();
    ReflectionTestUtils.setField(swimmingInstructor, "id", 1L);

    val swimmingClassConstructor = SwimmingClass.class.getDeclaredConstructor();
    swimmingClassConstructor.setAccessible(true);
    savedSwimmingClass = swimmingClassConstructor.newInstance();
    ReflectionTestUtils.setField(savedSwimmingClass, "id", 1L);
  }

  @Test
  @DisplayName("모든 조건이 충족되면 수영 클래스와 티켓이 성공적으로 생성된다.")
  void shouldCreateSwimmingClassAndTicketsSuccessfully() {
    // given
    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.of(swimmingPool));
    when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
        Optional.of(swimmingClassType));
    when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(
        Optional.of(swimmingClassSubType));
    when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(swimmingInstructor));
    when(swimmingClassRepository.save(any())).thenReturn(savedSwimmingClass);
    // when
    interactor.createBoSwimmingClass(command);
    // then
    verify(swimmingClassRepository, only()).save(assertArg(i -> {
      assertThat(i.getSwimmingPool()).isEqualTo(swimmingPool);
      assertThat(i.getYear()).isEqualTo(LocalDate.now().getYear());
      assertThat(i.getMonth()).isEqualTo(1);
      assertThat(i.getType()).isEqualTo(swimmingClassType);
      assertThat(i.getSubType()).isEqualTo(swimmingClassSubType);
      assertThat(i.getDaysOfWeek()).isEqualTo(84);
      assertThat(i.getStartTime()).isEqualTo(command.time().startTime());
      assertThat(i.getInstructor()).isEqualTo(swimmingInstructor);
      assertThat(i.getTotalCapacity()).isEqualTo(command.registrationCapacity().totalCapacity());
      assertThat(i.isVisible()).isEqualTo(command.isExposed());
    }));

    verify(swimmingClassTicketRepository, only()).saveAll(assertArg(i -> {
      assertThat(i).hasSize(2);
      assertThat(i).extracting(SwimmingClassTicket::getSwimmingClass)
          .containsOnly(savedSwimmingClass);
      assertThat(i).extracting(SwimmingClassTicket::getName).containsExactlyElementsOf(
          command.tickets().stream().map(Ticket::name).toList());
    }));
  }

  @Test
  @DisplayName("수영장이 존재하지 않으면 IllegalArgumentException을 던진다.")
  void shouldThrowIllegalArgumentExceptionWhenSwimmingPoolDoesNotExist() {
    // given
    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.createBoSwimmingClass(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("수영장이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("강습형태가 존재하지 않으면 IllegalArgumentException을 던진다.")
  void shouldThrowIllegalArgumentExceptionWhenSwimmingClassTypeDoesNotExist() {
    // given
    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.of(swimmingPool));
    when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.createBoSwimmingClass(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("강습형태가 존재하지 않습니다.");
  }

  @Test
  @DisplayName("강습구분이 존재하지 않으면 IllegalArgumentException을 던진다.")
  void shouldThrowIllegalArgumentExceptionWhenSwimmingClassSubTypeDoesNotExist() {
    // given
    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.of(swimmingPool));
    when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
        Optional.of(swimmingClassType));
    when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.createBoSwimmingClass(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("강습구분이 존재하지 않습니다.");
  }

  @Test
  @DisplayName("강사가 존재하지 않으면 IllegalArgumentException을 던진다.")
  void shouldThrowIllegalArgumentExceptionWhenInstructorDoesNotExist() {
    // given
    when(swimmingPoolRepository.findById(anyLong())).thenReturn(Optional.of(swimmingPool));
    when(swimmingClassTypeRepository.findById(anyLong())).thenReturn(
        Optional.of(swimmingClassType));
    when(swimmingClassSubTypeRepository.findById(anyLong())).thenReturn(
        Optional.of(swimmingClassSubType));
    when(instructorRepository.findById(anyLong())).thenReturn(Optional.empty());
    // when
    // then
    assertThatThrownBy(() -> interactor.createBoSwimmingClass(command))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("강사가 존재하지 않습니다.");
  }

  private static class CreateBoSwimmingClassCommandFactory {

    private static CreateBoSwimmingClassCommand create() {
      return CreateBoSwimmingClassCommand.builder()
          .swimmingPoolId(1L)
          .month(1)
          .days(List.of(MONDAY, WEDNESDAY, FRIDAY))
          .time(CreateBoSwimmingClassCommand.Time.builder()
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .build())
          .type(CreateBoSwimmingClassCommand.Type.builder()
              .classTypeId(1L)
              .classSubTypeId(1L)
              .build())
          .instructorId(1L)
          .tickets(List.of(
              CreateBoSwimmingClassCommand.Ticket.builder()
                  .name("티켓1")
                  .price(10000)
                  .build(),
              CreateBoSwimmingClassCommand.Ticket.builder()
                  .name("티켓2")
                  .price(20000)
                  .build()
          ))
          .registrationCapacity(CreateBoSwimmingClassCommand.RegistrationCapacity.builder()
              .totalCapacity(10)
              .reservationLimitCount(5)
              .build())
          .isExposed(true)
          .build();
    }
  }
}