package com.project.swimcb.oauth2.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.application.port.out.OAuth2MemberGateway;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.KakaoOAuth2Request;
import com.project.swimcb.oauth2.domain.Member;
import com.project.swimcb.oauth2.domain.OAuth2Member;
import com.project.swimcb.oauth2.domain.SignupRequest;
import com.project.swimcb.oauth2.domain.enums.Environment;
import com.project.swimcb.token.application.in.GenerateCustomerTokenUseCase;
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

  @Mock
  private GenerateCustomerTokenUseCase generateCustomerTokenUseCase;

  @Nested
  @DisplayName("유효한 OAuth2 요청이 주어졌을 때")
  class ValidOAuth2Request {

    private final String code = "valid_code";
    private final Environment env = Environment.LOCAL; // 예시로 LOCAL 환경을 사용
    private final KakaoOAuth2Request request = new KakaoOAuth2Request(code, env);
    private final OAuth2Member oAuth2Member = OAuth2Member.builder()
        .name("테스트 사용자")
        .email("test@example.com")
        .phoneNumber("010-1234-5678")
        .build();
    private Member member;
    private final String accessToken = "access_token";

    @BeforeEach
    void setUp() {
      member = Member.builder()
          .id(1L)
          .email(oAuth2Member.email())
          .name(oAuth2Member.name())
          .phoneNumber(oAuth2Member.phoneNumber())
          .build();
    }

    @Nested
    @DisplayName("이미 가입된 회원인 경우")
    class ExistingMember {

      @Test
      @DisplayName("로그인 응답을 반환해야 한다")
      void shouldReturnLoginResponse() {
        // given
        when(oAuth2MemberGateway.resolve(request.code())).thenReturn(oAuth2Member);
        when(findMemberPort.findByEmail(oAuth2Member.email())).thenReturn(
            Optional.of(member)); // 회원이 존재함
        when(generateCustomerTokenUseCase.generateCustomerToken(member.id()))
            .thenReturn(accessToken);

        val expectedResponse = mock(OAuth2Response.class);
        when(oAuth2Presenter.login(accessToken, env)).thenReturn(expectedResponse);

        // when
        val response = oAuth2Interactor.success(request);

        // then
        assertThat(response).isEqualTo(expectedResponse);
        verify(generateCustomerTokenUseCase).generateCustomerToken(member.id());
        verify(oAuth2Presenter).login(accessToken, request.env());
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
        val savedMemberId = 1L;

        when(oAuth2MemberGateway.resolve(request.code())).thenReturn(oAuth2Member);
        when(findMemberPort.findByEmail(oAuth2Member.email())).thenReturn(
            Optional.empty()); // 회원이 존재하지 않음
        when(signupPort.signup(any(SignupRequest.class))).thenReturn(savedMemberId); // 회원가입 성공
        when(generateCustomerTokenUseCase.generateCustomerToken(savedMemberId)).thenReturn(
            accessToken);

        val expectedResponse = mock(OAuth2Response.class);
        when(oAuth2Presenter.signup(accessToken, env)).thenReturn(expectedResponse);

        // when
        val response = oAuth2Interactor.success(request);

        // then
        assertThat(response).isEqualTo(expectedResponse);

        verify(oAuth2Presenter).signup(accessToken, request.env());
        verify(oAuth2Presenter, never()).login(anyString(), any());
      }

      @Test
      @DisplayName("회원가입 요청은 올바른 정보를 포함해야 한다")
      void shouldCreateSignupRequestWithCorrectInfo() {
        // given
        val savedMemberId = 1L;

        when(oAuth2MemberGateway.resolve(request.code())).thenReturn(oAuth2Member);
        when(findMemberPort.findByEmail(oAuth2Member.email())).thenReturn(Optional.empty());
        when(signupPort.signup(any(SignupRequest.class))).thenReturn(1L);
        when(generateCustomerTokenUseCase.generateCustomerToken(savedMemberId)).thenReturn(
            accessToken);
        when(oAuth2Presenter.signup(accessToken, request.env())).thenReturn(
            mock(OAuth2Response.class));

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
    private final Environment environment = Environment.LOCAL; // 예시로 LOCAL 환경을 사용
    private final KakaoOAuth2Request request = new KakaoOAuth2Request(invalidCode, environment);

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
      verify(oAuth2Presenter, never()).login(anyString(), any());
      verify(oAuth2Presenter, never()).signup(anyString(), any());
    }

  }

}
