package com.project.swimcb.swimmingpool.application;

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
import com.project.swimcb.swimmingpool.domain.ReservationRepository;
import com.project.swimcb.swimmingpool.domain.ReserveSwimmingClassCommand;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    @DisplayName("성공적으로 예약을 생성한다.")
    void shouldCreateSwimmingClassSuccessfully() {
      // given
      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.of(swimmingClass));
      when(swimmingClass.isFull()).thenReturn(false);
      when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
      // when
      interactor.reserveSwimmingClass(command);
      // then
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
    @DisplayName("수영 클래스가 가득 찼다면 NoSuchElementException를 발생시킨다.")
    void shouldThrowNoSuchElementExceptionWhenSwimmingClassIsFull() {
      // given
      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.of(swimmingClass));
      when(swimmingClass.isFull()).thenReturn(true);
      // when
      // then
      assertThatThrownBy(() -> interactor.reserveSwimmingClass(command))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("예약 정원을 초과하였습니다. : " + command.swimmingClassId());
    }

    @Test
    @DisplayName("존재하지 않는 회원이라면 NoSuchElementException를 발생시킨다.")
    void shouldThrowNoSuchElementExceptionWhenMemberDoesNotExist() {
      // given
      when(swimmingClassRepository.findById(anyLong())).thenReturn(Optional.of(swimmingClass));
      when(swimmingClass.isFull()).thenReturn(false);
      when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());
      // when
      // then
      assertThatThrownBy(() -> interactor.reserveSwimmingClass(command))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessage("회원이 존재하지 않습니다 : " + command.memberId());
    }
  }
}