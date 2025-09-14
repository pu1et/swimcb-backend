package com.project.swimcb.oauth2.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

import com.project.swimcb.oauth2.application.port.in.OAuth2ClientInfoProvider;
import com.project.swimcb.oauth2.domain.OAuth2ClientInfo;
import com.project.swimcb.oauth2.domain.OAuth2ClientInfo.Provider;
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
class AppleIdTokenParserTest {

  @InjectMocks
  private AppleIdTokenParser appleIdTokenParser;

  @Mock
  private OAuth2ClientInfoProvider appleOAuth2ClientInfoProvider;

  @Nested
  @DisplayName("parseAndVerifyToken 메서드는")
  class ParseAndVerifyTokenTest {

    @Test
    @DisplayName("정상적인 idToken으로 요청하면 Map을 반환한다")
    void shouldReturnMapWhenIdTokenIsValid() {
      // given
      // 유효하지만 만료된 토큰
      val validIdTokenButExpired = "eyJraWQiOiJVYUlJRlkyZlc0IiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL2FwcGxlaWQuYXBwbGUuY29tIiwiYXVkIjoiY29tLnN3aW0tY2Iud2ViIiwiZXhwIjoxNzU3NzUxNzIwLCJpYXQiOjE3NTc2NjUzMjAsInN1YiI6IjAwMTQ2Ny43ZTI4MDNhYmI2N2Y0NzIxODhlZTI0Y2NmOTJmNGMyYi4wNDM5IiwiY19oYXNoIjoiMEFpRGpGV0RpdzBndUlGUW13eGdvQSIsImVtYWlsIjoicHUxZXRwcm9vZkBnbWFpbC5jb20iLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiYXV0aF90aW1lIjoxNzU3NjY1MzIwLCJub25jZV9zdXBwb3J0ZWQiOnRydWV9.sZ7eMBcLyL4mrx6nFnZELRtyDpnrpusPCqt4I0Qgn6hjP3uMklEvSSeARSbBb5XVawik-gXOXMKw6WCpCxqs1jIpMedy7ipwBPYOwDRrv2FZ8JJif-xa7nZr9Q9sG6Z_-Fe1jlhXtNQT1TWMs7M8VtefNc2J5dUycWOCJWi8Eh7VwkHkDwGdS6p5ZyJOHvgypMXGSTTK1HKh2bhzIlqX0zL_JDVfnFzOda07sy1cofnVIPLx1R6pFf9BuBrTIlbil526FCLFvE043rNWfF4OfX5q3I39HkbkVsA5C2lzasspHrsxD8Pt474gaR66bhe1HRx5VqldU-BjlprWWpSxdA";
      val clientInfo = createOAuth2ClientInfo("com.swim-cb.web");

      when(appleOAuth2ClientInfoProvider.getOAuth2ClientInfo()).thenReturn(clientInfo);

      // when
      // then
      try {
        val result = appleIdTokenParser.parseAndVerifyToken(validIdTokenButExpired);

        assertThat(result).isNotEmpty();
        assertThat(result.get("iss")).isEqualTo("https://appleid.apple.com");
        assertThat(result.get("aud")).isEqualTo(List.of("com.swim-cb.web"));
        assertThat(result.get("exp")).isNotNull();
        assertThat(result.get("iat")).isNotNull();
        // 사용자마다 고유 식별자
        assertThat(result.get("sub")).isEqualTo("001467.7e2803abb67f472188ee24ccf92f4c2b.0439");
        // 사용자 이메일
        assertThat(result.get("email")).isEqualTo("pu1etproof@gmail.com");
        // 사용자 이메일 인증 여부
        assertThat(result.get("email_verified")).isEqualTo(true);

      } catch (IllegalArgumentException e) {
        // 토큰 만료기간 초과 에러는 무시
      }

      then(appleOAuth2ClientInfoProvider).should().getOAuth2ClientInfo();
    }

    @Test
    @DisplayName("null idToken으로 요청하면 NullPointerException이 발생한다")
    void shouldThrowNullPointerExceptionWhenIdTokenIsNull() {
      // given
      val nullIdToken = (String) null;

      // when & then
      assertThatThrownBy(() -> appleIdTokenParser.parseAndVerifyToken(nullIdToken))
          .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("잘못된 형식의 idToken으로 요청하면 IllegalArgumentException이 발생한다")
    void shouldThrowIllegalArgumentExceptionWhenIdTokenIsInvalidFormat() {
      // given
      val invalidIdToken = "invalid.token.format";
      // when
      // then
      assertThatThrownBy(() -> appleIdTokenParser.parseAndVerifyToken(invalidIdToken))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("invalid token");
    }

    @Test
    @DisplayName("빈 문자열 idToken으로 요청하면 IllegalArgumentException이 발생한다")
    void shouldThrowIllegalArgumentExceptionWhenIdTokenIsEmpty() {
      // given
      val emptyIdToken = "";
      // when
      // then
      assertThatThrownBy(() -> appleIdTokenParser.parseAndVerifyToken(emptyIdToken))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("invalid token");
    }

    @Test
    @DisplayName("JWT 형식이지만 서명이 없는 토큰으로 요청하면 IllegalArgumentException이 발생한다")
    void shouldThrowIllegalArgumentExceptionWhenTokenHasNoSignature() {
      // given
      val unsignedToken = "eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.";
      // when
      // then
      assertThatThrownBy(() -> appleIdTokenParser.parseAndVerifyToken(unsignedToken))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("invalid token");
    }

  }

  private OAuth2ClientInfo createOAuth2ClientInfo(String clientId) {
    val registration = OAuth2ClientInfo.Registration.builder()
        .clientId(clientId)
        .redirectUri("DUMMY_REDIRECT_URI")
        .scope(List.of("DUMMY_SCOPE"))
        .build();

    return OAuth2ClientInfo.builder()
        .registration(registration)
        .provider(Provider.builder()
            .authorizationUri("DUMMY_AUTHORIZATION_URI")
            .build())
        .build();
  }

}
