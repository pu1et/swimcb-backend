package com.project.swimcb.oauth2.adapter.out;

import static com.project.swimcb.oauth2.domain.enums.OAuth2ProviderType.KAKAO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.db.entity.MemberEntity;
import com.project.swimcb.db.entity.OAuth2Provider;
import com.project.swimcb.db.repository.MemberRepository;
import com.project.swimcb.oauth2.domain.SignupRequest;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SignupInteractorTest {

  @InjectMocks
  private SignupInteractor signupInteractor;

  @Mock
  private MemberRepository memberRepository;

  @Nested
  @DisplayName("회원가입 요청 처리 시")
  class SignupMethod {

    @Test
    @DisplayName("유효한 가입 요청이 주어지면 회원을 저장해야 한다")
    void shouldSaveMemberWhenGivenValidSignupRequest() {
      // given
      val request = SignupRequest.builder()
          .name("테스트 사용자")
          .email("test@example.com")
          .phoneNumber("010-1234-5678")
          .providerType(KAKAO)
          .build();

      val savedMember = mock(MemberEntity.class);
      when(savedMember.getId()).thenReturn(1L);
      when(memberRepository.save(any(MemberEntity.class))).thenReturn(savedMember);

      // when
      val memberId = signupInteractor.signup(request);

      // then
      assertThat(memberId).isEqualTo(savedMember.getId());

      verify(memberRepository).save(assertArg(i -> {
        assertThat(i.getName()).isEqualTo(request.name());
        assertThat(i.getEmail()).isEqualTo(request.email());
        assertThat(i.getPhone()).isEqualTo(request.phoneNumber());
        assertThat(i.getProvider()).isEqualTo(OAuth2Provider.KAKAO);
        assertThat(i.getNickname()).isEqualTo("닉네임");
      }));
    }

    @Test
    @DisplayName("Null 요청이 주어지면 NullPointerException을 발생시켜야 한다")
    void shouldThrowNullPointerExceptionWhenGivenNullSignupRequest() {
      // when
      // then
      assertThatThrownBy(() -> signupInteractor.signup(null))
          .isInstanceOf(NullPointerException.class);

      verify(memberRepository, never()).save(any());
    }

  }

}
