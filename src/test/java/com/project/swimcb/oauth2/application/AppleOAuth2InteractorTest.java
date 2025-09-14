package com.project.swimcb.oauth2.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;

import com.project.swimcb.oauth2.adapter.in.OAuth2Response;
import com.project.swimcb.oauth2.application.port.out.FindMemberPort;
import com.project.swimcb.oauth2.application.port.out.OAuth2Presenter;
import com.project.swimcb.oauth2.application.port.out.SignupPort;
import com.project.swimcb.oauth2.domain.AppleOAuth2Request;
import com.project.swimcb.oauth2.domain.Member;
import com.project.swimcb.oauth2.domain.SignupRequest;
import com.project.swimcb.token.application.in.GenerateCustomerTokenUseCase;
import java.util.Optional;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.view.RedirectView;

@ExtendWith(MockitoExtension.class)
class AppleOAuth2InteractorTest {

  @InjectMocks
  private AppleOAuth2Interactor appleOAuth2Interactor;

  @Mock
  private FindMemberPort findMemberPort;

  @Mock
  private SignupPort signupPort;

  @Mock
  private OAuth2Presenter oAuth2Presenter;

  @Mock
  private GenerateCustomerTokenUseCase generateCustomerTokenUseCase;

  @Nested
  @DisplayName("Apple OAuth2 인증 성공 처리는")
  class SuccessMethod {

    @Test
    @DisplayName("회원이 존재하지 않는 경우 회원가입을 진행하고 토큰을 생성한다")
    void shouldSignupWhenMemberNotExists() {
      // given
      val email = "test@apple.com";
      val request = new AppleOAuth2Request(email);
      val memberId = 1L;
      val token = "DUMMY_TEST_TOKEN";
      val redirectView = mock(RedirectView.class);
      val expectedResponse = new OAuth2Response(redirectView);

      given(findMemberPort.findByEmail(email)).willReturn(Optional.empty());
      given(signupPort.signup(SignupRequest.builder().email(email).build())).willReturn(memberId);
      given(generateCustomerTokenUseCase.generateCustomerToken(memberId)).willReturn(token);
      given(oAuth2Presenter.signup(token, null)).willReturn(expectedResponse);

      // when
      val result = appleOAuth2Interactor.success(request);

      // then
      assertThat(result).isEqualTo(expectedResponse);

      then(findMemberPort).should(only()).findByEmail(email);
      then(signupPort).should().signup(SignupRequest.builder().email(email).build());
      then(generateCustomerTokenUseCase).should().generateCustomerToken(memberId);
      then(oAuth2Presenter).should().signup(token, null);
    }

    @Test
    @DisplayName("회원이 존재하는 경우 로그인을 진행하고 토큰을 생성한다")
    void shouldLoginWhenMemberExists() {
      // given
      val email = "existing@apple.com";
      val request = new AppleOAuth2Request(email);
      val memberId = 2L;
      val member = mock(Member.class);
      val token = "DUMMY_TEST_TOKEN";
      val redirectView = mock(RedirectView.class);
      val expectedResponse = new OAuth2Response(redirectView);

      given(member.id()).willReturn(memberId);
      given(findMemberPort.findByEmail(email)).willReturn(Optional.of(member));
      given(generateCustomerTokenUseCase.generateCustomerToken(memberId)).willReturn(token);
      given(oAuth2Presenter.login(token, null)).willReturn(expectedResponse);

      // when
      val result = appleOAuth2Interactor.success(request);

      // then
      assertThat(result).isEqualTo(expectedResponse);

      then(findMemberPort).should(only()).findByEmail(email);
      then(generateCustomerTokenUseCase).should().generateCustomerToken(memberId);
      then(oAuth2Presenter).should().login(token, null);

      then(signupPort).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("null 요청으로 호출 시 NullPointerException이 발생한다")
    void shouldThrowNullPointerExceptionWhenRequestIsNull() {
      // given
      // when
      // then
      assertThatThrownBy(() -> appleOAuth2Interactor.success(null))
          .isInstanceOf(NullPointerException.class);
    }

  }

}
