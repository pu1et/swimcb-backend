package com.project.swimcb.mypage.member.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.repository.MemberRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WithdrawInteractorTest {

  @InjectMocks
  private WithdrawInteractor interactor;

  @Mock
  private MemberRepository memberRepository;

  @Nested
  @DisplayName("회원 탈퇴")
  class Withdraw {

    @Nested
    @DisplayName("유효한 회원 ID가 주어진 경우")
    class WhenValidMemberIdGiven {

      @Test
      @DisplayName("회원을 조회하고 탈퇴 처리한다")
      void shouldFindMemberAndWithdraw() {
        // given
        val memberId = 1L;
        val member = mock(MemberEntity.class);
        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        interactor.withdraw(memberId);

        // then
        then(memberRepository).should().findById(memberId);
        then(member).should().withdraw();
      }
    }

    @Nested
    @DisplayName("존재하지 않는 회원 ID가 주어진 경우")
    class WhenNonExistentMemberIdGiven {

      @Test
      @DisplayName("NoSuchElementException을 발생시킨다")
      void shouldThrowNoSuchElementException() {
        // given
        val memberId = 999L;
        given(memberRepository.findById(memberId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> interactor.withdraw(memberId))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessage("해당 회원이 존재하지 않습니다 : " + memberId);

        then(memberRepository).should().findById(memberId);
      }
    }

    @Nested
    @DisplayName("null 회원 ID가 주어진 경우")
    class WhenNullMemberIdGiven {

      @Test
      @DisplayName("NullPointerException을 발생시킨다")
      void shouldThrowNullPointerException() {
        // given
        final Long memberId = null;

        // when & then
        assertThatThrownBy(() -> interactor.withdraw(memberId))
            .isInstanceOf(NullPointerException.class);
      }
    }
  }
}
