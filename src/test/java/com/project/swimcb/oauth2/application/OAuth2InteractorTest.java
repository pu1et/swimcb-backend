package com.project.swimcb.oauth2.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.oauth2.adapter.in.OAuth2Request;
import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.application.port.out.OAuth2MemberGateway;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.Member;
import com.project.swimcb.oauth2.domain.OAuth2Member;
import com.project.swimcb.oauth2.domain.SignupRequest;
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
class OAuth2InteractorTest {

  @InjectMocks
  private OAuth2Interactor oAuth2Interactor;

  @Mock
  private OAuth2MemberGateway oAuth2MemberGateway;

  @Mock
  private FindMemberPort findMemberPort;

  @Mock
  private SignupPort signupPort;

  @Mock
  private OAuth2Presenter oAuth2Presenter;

  @Nested
  @DisplayName("유효한 OAuth2 요청이 주어졌을 때")
  class ValidOAuth2Request {

    private final String code = "valid_code";
    private final OAuth2Request request = new OAuth2Request(code);
    private final OAuth2Member oAuth2Member = OAuth2Member.builder()
        .name("테스트 사용자")
        .email("test@example.com")
        .phoneNumber("010-1234-5678")
        .build();
    private final Member member = mock(Member.class);

    @Nested
    @DisplayName("이미 가입된 회원인 경우")
    class ExistingMember {

      @Test
      @DisplayName("로그인 응답을 반환해야 한다")
      void shouldReturnLoginResponse() {
        // given
        when(oAuth2MemberGateway.resolve(code)).thenReturn(oAuth2Member);
        when(findMemberPort.findByEmail(oAuth2Member.email())).thenReturn(Optional.of(member)); // 회원이 존재함

        val expectedResponse = mock(OAuth2Response.class);
        when(oAuth2Presenter.login()).thenReturn(expectedResponse);

        // when
        val response = oAuth2Interactor.success(request);

        // then
        assertThat(response).isEqualTo(expectedResponse);
        verify(oAuth2MemberGateway).resolve(code);
        verify(findMemberPort).findByEmail(oAuth2Member.email());
        verify(oAuth2Presenter).login();
        verify(signupPort, never()).signup(any());
      }
    }

    @Nested
    @DisplayName("신규 회원인 경우")
    class NewMember {

      @Test
      @DisplayName("회원가입 후 가입 응답을 반환해야 한다")
      void shouldSignupAndReturnSignupResponse() {
        // given
        when(oAuth2MemberGateway.resolve(code)).thenReturn(oAuth2Member);
        when(findMemberPort.findByEmail(oAuth2Member.email())).thenReturn(Optional.empty()); // 회원이 존재하지 않음

        val expectedResponse = mock(OAuth2Response.class);
        when(oAuth2Presenter.signup()).thenReturn(expectedResponse);

        // when
        val response = oAuth2Interactor.success(request);

        // then
        assertThat(response).isEqualTo(expectedResponse);
        verify(oAuth2MemberGateway).resolve(code);
        verify(findMemberPort).findByEmail(oAuth2Member.email());
        verify(signupPort).signup(any(SignupRequest.class));
        verify(oAuth2Presenter).signup();
        verify(oAuth2Presenter, never()).login();
      }

      @Test
      @DisplayName("회원가입 요청은 올바른 정보를 포함해야 한다")
      void shouldCreateSignupRequestWithCorrectInfo() {
        // given
        when(oAuth2MemberGateway.resolve(code)).thenReturn(oAuth2Member);
        when(findMemberPort.findByEmail(oAuth2Member.email())).thenReturn(Optional.empty());
        when(oAuth2Presenter.signup()).thenReturn(mock(OAuth2Response.class));

        // when
        oAuth2Interactor.success(request);

        // then
        verify(signupPort).signup(argThat(signupRequest ->
            signupRequest.email().equals(oAuth2Member.email()) &&
                signupRequest.name().equals(oAuth2Member.name()) &&
                signupRequest.phoneNumber().equals(oAuth2Member.phoneNumber())
        ));
      }
    }
  }

  @Nested
  @DisplayName("유효하지 않은 OAuth2 요청이 주어졌을 때")
  class InvalidOAuth2Request {

    private final String invalidCode = "invalid_code";
    private final OAuth2Request request = new OAuth2Request(invalidCode);

    @Test
    @DisplayName("OAuth2 게이트웨이에서 예외가 발생하면 그대로 전파되어야 한다")
    void shouldPropagateExceptionFromGateway() {
      // given
      val expectedException = new RuntimeException("유효하지 않은 인증 코드");
      when(oAuth2MemberGateway.resolve(invalidCode)).thenThrow(expectedException);

      // when
      // then
      assertThatThrownBy(() -> oAuth2Interactor.success(request))
          .isEqualTo(expectedException);

      verify(findMemberPort, never()).findByEmail(any());
      verify(signupPort, never()).signup(any());
      verify(oAuth2Presenter, never()).login();
      verify(oAuth2Presenter, never()).signup();
    }
  }
}
