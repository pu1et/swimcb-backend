package com.project.swimcb.mypage.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.repository.MemberRepository;
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
class FindMemberInfoInteractorTest {

  @InjectMocks
  private FindMemberInfoInteractor interactor;

  @Mock
  private MemberRepository repository;

  @Mock
  private MemberEntity memberEntity;

  @Test
  @DisplayName("회원이 존재하면 회원 정보를 반환한다")
  void shouldReturnMemberInfoWhenMemberExists() {
    // given
    val memberId = 1L;

    given(repository.findById(memberId)).willReturn(Optional.of(memberEntity));
    given(memberEntity.getName()).willReturn("홍길동");
    given(memberEntity.getPhone()).willReturn("010-1234-5678");
    given(memberEntity.getEmail()).willReturn("test@example.com");

    // when
    val result = interactor.findMemberInfo(memberId);

    // then
    assertThat(result.name()).isEqualTo("홍길동");
    assertThat(result.phone()).isEqualTo("010-1234-5678");
    assertThat(result.email()).isEqualTo("test@example.com");
    then(repository).should().findById(memberId);
  }

  @Test
  @DisplayName("회원이 존재하지 않으면 예외를 발생시킨다")
  void shouldThrowExceptionWhenMemberNotExists() {
    // given
    val memberId = 999L;
    given(repository.findById(memberId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> interactor.findMemberInfo(memberId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("회원이 존재하지 않습니다 : " + memberId);
  }

}
