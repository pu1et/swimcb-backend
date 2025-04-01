package com.project.swimcb.swimmingpool.application;

import static com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus.NOT_RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus.RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.SwimmingClassReservationStatus.WAITING_RESERVABLE;
import static com.project.swimcb.swimmingpool.domain.enums.PaymentMethod.BANK_TRANSFER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.domain.SwimmingClass;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import com.project.swimcb.member.MemberRepository;
import com.project.swimcb.member.domain.Member;
import com.project.swimcb.swimmingpool.domain.ReservationInfo;
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import com.project.swimcb.swimmingpool.domain.ReserveSwimmingClassCommand;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReserveSwimmingClassInteractorTest {

  @InjectMocks
  private ReserveSwimmingClassInteractor interactor;

  @Mock
  private SwimmingClassRepository swimmingClassRepository;

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private MemberRepository memberRepository;

  @Nested
  @DisplayName("수영 클래스 예약시")
  class ReserveSwimmingClassTest {

    private final long MEMBER_ID = 1L;
    private final long SWIMMING_CLASS_ID = 2L;
    private final long TICKET_ID = 3L;

    private ReserveSwimmingClassCommand command;
    private SwimmingClass swimmingClass;
    private Member member;

    @BeforeEach
    void setUp() {
      command = ReserveSwimmingClassCommand.builder()
          .memberId(MEMBER_ID)
          .swimmingClassId(SWIMMING_CLASS_ID)
          .ticketId(TICKET_ID)
          .paymentMethod(BANK_TRANSFER)
          .build();

      swimmingClass = mock(SwimmingClass.class);
      member = mock(Member.class);
    }

    @Test
    @DisplayName("일반 예약이 가능한 상태면 일반 예약을 생성한다")
    void shouldCreateNormalReservationWhenReservable() {
      // given
      val reservationStatus = RESERVABLE;

      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.of(swimmingClass));
      when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
      when(swimmingClass.getReservationStatus()).thenReturn(reservationStatus);

      // when
      val result = interactor.reserveSwimmingClass(command);

      // then
      assertThat(result.status()).isEqualTo(reservationStatus);
      assertThat(result.waitingNo()).isNull();

      verify(swimmingClassRepository, only()).findById(command.swimmingClassId());
      verify(memberRepository, only()).findById(command.memberId());
      verify(reservationRepository, only()).save(assertArg(i -> {
        assertThat(i.getMember()).isEqualTo(member);
        assertThat(i.getTicketId()).isEqualTo(command.ticketId());
        assertThat(i.getPaymentMethod()).isEqualTo(command.paymentMethod());
      }));
      verify(swimmingClass, times(1)).increaseReservedCount();
    }

    @Test
    @DisplayName("대기 예약 상태면 대기 예약을 생성한다")
    void shouldCreateWaitingReservationWhenWaitingReservable() {
      // given
      val waitingNum = 1;

      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.of(swimmingClass));
      when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
      when(swimmingClass.getReservationStatus()).thenReturn(WAITING_RESERVABLE);
      when(swimmingClass.calculateWaitingNum()).thenReturn(waitingNum);

      // when
      ReservationInfo result = interactor.reserveSwimmingClass(command);

      // then
      assertThat(result.status()).isEqualTo(WAITING_RESERVABLE);
      assertThat(result.waitingNo()).isEqualTo(waitingNum);

      verify(reservationRepository, only()).save(assertArg(i -> {
        assertThat(i.getMember()).isEqualTo(member);
        assertThat(i.getTicketId()).isEqualTo(command.ticketId());
        assertThat(i.getPaymentMethod()).isEqualTo(command.paymentMethod());
        assertThat(i.getWaitingNo()).isEqualTo(waitingNum);
      }));
      verify(swimmingClass, times(1)).increaseReservedCount();
    }

    @Test
    @DisplayName("예약 불가능 상태면 예외를 발생시킨다")
    void shouldThrowExceptionWhenNotReservable() {
      // given
      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.of(swimmingClass));
      when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
      when(swimmingClass.getReservationStatus()).thenReturn(NOT_RESERVABLE);

      // when & then
      assertThatThrownBy(() -> interactor.reserveSwimmingClass(command))
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("대기 예약도 불가능한 상태입니다.");
    }

    @Test
    @DisplayName("존재하지 않는 수영 클래스라면 NoSuchElementException를 발생시킨다.")
    void shouldThrowNoSuchElementExceptionWhenSwimmingClassDoesNotExist() {
      // given
      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.empty());
      // when
      // then
      assertThatThrownBy(() -> interactor.reserveSwimmingClass(command))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("수영 클래스가 존재하지 않습니다 : " + command.swimmingClassId());
    }

    @Test
    @DisplayName("존재하지 않는 회원이라면 NoSuchElementException를 발생시킨다.")
    void shouldThrowNoSuchElementExceptionWhenMemberDoesNotExist() {
      // given
      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.of(swimmingClass));
      when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());
      // when
      // then
      assertThatThrownBy(() -> interactor.reserveSwimmingClass(command))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("회원이 존재하지 않습니다 : " + command.memberId());
    }
  }
}