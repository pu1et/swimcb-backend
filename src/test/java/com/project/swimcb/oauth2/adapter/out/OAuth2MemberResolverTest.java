package com.project.swimcb.oauth2.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * 아래 링크를 통해 code를 얻고 테스트를 실행해야합니다. <p>
 * <a href="http://localhost:8080/api/oauth2/authorization/kakao">kakao oauth</a>
 */
@Disabled("code를 kakao로부터 얻어서 사용하세요!")
@SpringBootTest
@ActiveProfiles("dev")
class OAuth2MemberResolverTest {

  @Autowired
  private OAuth2MemberResolver resolver;

  @Test
  @DisplayName("카카오 데이터 확인")
  void checkKakaoData() {
    // given
    val code = "주석에있는_링크를_통해_code를_얻어주세요";
    // when
    val member = resolver.resolve(code);
    // then
    assertThat(member).isNotNull();
    assertThat(member.name()).isNotNull();
    assertThat(member.email()).isNotNull();
    assertThat(member.phoneNumber()).isNotNull();
  }

}
