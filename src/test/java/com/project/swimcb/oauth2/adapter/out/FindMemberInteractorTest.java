package com.project.swimcb.oauth2.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.oauth2.domain.Member;
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
class FindMemberInteractorTest {

  @InjectMocks
  private FindMemberInteractor interactor;

  @Mock
  private MemberRepository memberRepository;

  @Nested
  @DisplayName("이메일로 회원 조회 시")
  class FindByEmail {

    private final String existingEmail = "test@example.com";
    private final String nonExistingEmail = "nonexisting@example.com";

    @Test
    @DisplayName("존재하는 이메일로 조회하면 회원을 반환해야 한다")
    void shouldReturnMemberwhenEmailExists() {
      // given
      val memberEntity = mock(MemberEntity.class);
      when(memberEntity.getId()).thenReturn(1L);
      when(memberEntity.getEmail()).thenReturn(existingEmail);
      when(memberEntity.getName()).thenReturn("테스트 사용자");
      when(memberEntity.getPhone()).thenReturn("010-1234-5678");

      when(memberRepository.findByEmail(existingEmail)).thenReturn(Optional.of(memberEntity));

      // when
      Optional<Member> result = interactor.findByEmail(existingEmail);

      // then
      assertThat(result).isPresent();
      Member member = result.get();
      assertThat(member.id()).isEqualTo(1L);
      assertThat(member.email()).isEqualTo(existingEmail);
      assertThat(member.name()).isEqualTo("테스트 사용자");
      assertThat(member.phoneNumber()).isEqualTo("010-1234-5678");

      verify(memberRepository).findByEmail(existingEmail);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 조회하면 빈 Optional을 반환해야 한다")
    void shouldReturnEmptyOptionalWhenEmailDoesNotExist() {
      // given
      when(memberRepository.findByEmail(nonExistingEmail)).thenReturn(Optional.empty());

      // when
      Optional<Member> result = interactor.findByEmail(nonExistingEmail);

      // then
      assertThat(result).isEmpty();
      verify(memberRepository).findByEmail(nonExistingEmail);
    }

    @Test
    @DisplayName("null 이메일로 조회하면 NullPointerException을 발생시켜야 한다")
    void shouldThrowNullPointerExceptionWhenEmailIsNull() {
      // when/then
      assertThatThrownBy(() -> interactor.findByEmail(null))
          .isInstanceOf(NullPointerException.class);

      verify(memberRepository, never()).findByEmail(any());
    }
  }
}
