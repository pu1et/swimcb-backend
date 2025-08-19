package com.project.swimcb.bo.clone.application;

import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SUNDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;

import com.project.swimcb.bo.clone.application.port.out.CopySwimmingClassDsGateway;
import com.project.swimcb.bo.clone.domain.CopySwimmingClassCommand;
import com.project.swimcb.bo.clone.domain.SwimmingClassCopyCandidate;
import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassUseCase;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CopySwimmingClassInteractorTest {

  @InjectMocks
  private CopySwimmingClassInteractor interactor;

  @Mock
  private CopySwimmingClassDsGateway gateway;

  @Mock
  private CreateBoSwimmingClassUseCase createBoFreeSwimmingUseCase;

  @Nested
  @DisplayName("수영 클래스 복사 시")
  class CopySwimmingClassTests {

    @Test
    @DisplayName("수영 클래스 복사를 성공적으로 수행한다")
    void shouldCopySwimmingClassSuccessfully() {
      // given
      val command = TestCopySwimmingClassCommandFactory.create();
      val candidates = TestSwimmingClassCopyCandidateFactory.createList();

      given(gateway.findAllSwimmingClassesByMonth(command.fromMonth()))
          .willReturn(candidates);

      // when
      interactor.copySwimmingClass(command);

      // then
      then(gateway).should(times(1)).deleteSwimmingClassByMonth(command.toMonth());
      then(gateway).should(times(1)).findAllSwimmingClassesByMonth(command.fromMonth());
      then(createBoFreeSwimmingUseCase)
          .should(times(2))
          .createBoSwimmingClass(assertArg(i -> {
            assertThat(i.swimmingPoolId()).isIn(1L, 2L);
            assertThat(i.month()).isEqualTo(command.toMonth().getMonthValue());
            assertThat(i.days()).isEqualTo(List.of(MONDAY, SUNDAY));
            assertThat(i.time().startTime()).isEqualTo(LocalTime.of(9, 0));
            assertThat(i.type().classTypeId()).isEqualTo(2L);
            assertThat(i.instructorId()).isEqualTo(4L);
            assertThat(i.tickets()).hasSize(1);
            assertThat(i.tickets().getFirst().name()).isEqualTo("MOCK_TICKET");
            assertThat(i.tickets().getFirst().price()).isEqualTo(50000);
            assertThat(i.registrationCapacity().totalCapacity()).isEqualTo(10);
          }));
    }

    @Test
    @DisplayName("복사 클래스 후보 리스트가 빈 리스트이면 이후 로직을 수행하지 않는다")
    void shouldHandleEmptyCandidateList() {
      // given
      val command = TestCopySwimmingClassCommandFactory.create();
      val emptyCandidates = List.<SwimmingClassCopyCandidate>of();

      given(gateway.findAllSwimmingClassesByMonth(command.fromMonth()))
          .willReturn(emptyCandidates);

      // when
      interactor.copySwimmingClass(command);

      // then
      then(gateway).should(only()).findAllSwimmingClassesByMonth(command.fromMonth());
      then(createBoFreeSwimmingUseCase).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("null 파라미터가 전달되면 NullPointerException을 던진다")
    void shouldThrowExceptionWhenCommandIsNull() {
      // when
      // then
      assertThatThrownBy(() -> interactor.copySwimmingClass(null))
          .isInstanceOf(NullPointerException.class);
    }

  }

  private static class TestCopySwimmingClassCommandFactory {

    private static CopySwimmingClassCommand create() {
      return new CopySwimmingClassCommand(
          YearMonth.of(2025, 1),
          YearMonth.of(2025, 2)
      );
    }

  }

  private static class TestSwimmingClassCopyCandidateFactory {

    private static List<SwimmingClassCopyCandidate> createList() {
      return List.of(
          createCandidate(1L, 1),
          createCandidate(2L, 1)
      );
    }

    private static SwimmingClassCopyCandidate createCandidate(long swimmingPoolId, int month) {
      return SwimmingClassCopyCandidate.builder()
          .swimmingPoolId(swimmingPoolId)
          .month(month)
          .dayOfWeek(ClassDayOfWeek.of(0b1000001))
          .time(SwimmingClassCopyCandidate.Time.builder()
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .build())
          .type(SwimmingClassCopyCandidate.Type.builder()
              .classTypeId(2L)
              .classSubTypeId(3L)
              .build())
          .instructorId(4L)
          .tickets(List.of(
              SwimmingClassCopyCandidate.Ticket.builder()
                  .name("MOCK_TICKET")
                  .price(50000)
                  .build()
          ))
          .registrationCapacity(SwimmingClassCopyCandidate.RegistrationCapacity.builder()
              .totalCapacity(10)
              .reservationLimitCount(5)
              .build())
          .build();
    }

  }

}
