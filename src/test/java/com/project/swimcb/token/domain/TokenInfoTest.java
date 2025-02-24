package com.project.swimcb.token.domain;

import static com.project.swimcb.token.domain.enums.MemberRole.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.project.swimcb.token.domain.enums.MemberRole;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TokenInfoTest {

  @Test
  @DisplayName("게스트 토큰 정보 생성시 memberId는 null이고 role은 GUEST이다.")
  void shouldHaveNullMemberIdAndGuestRoleWhenGuestTokenIsGenerated() {
    // given
    // when
    val guestTokenInfo = TokenInfo.guest();
    // then
    assertThat(guestTokenInfo.memberId()).isNull();
    assertThat(guestTokenInfo.role()).isEqualTo(GUEST);
  }

  @Test
  @DisplayName("회원 - 고객 토큰 정보 생성시 memberId는 null이 아니고 role은 CUSTOMER이다.")
  void shouldNotHaveNullMemberIdAndCustomerRoleWhenCustomerTokenIsGenerated() {
    // given
    val memberId = 1L;
    // when
    val customerTokenInfo = TokenInfo.customer(memberId);
    // then
    assertThat(customerTokenInfo.memberId()).isEqualTo(memberId);
    assertThat(customerTokenInfo.role()).isEqualTo(CUSTOMER);
  }

  @Test
  @DisplayName("회원 - 관리자 토큰 정보 생성시 memberId는 null이 아니고 role은 ADMIN이다.")
  void shouldNotHaveNullMemberIdAndAdminRoleWhenAdminTokenIsGenerated() {
    // given
    val memberId = 1L;
    // when
    val adminTokenInfo = TokenInfo.admin(memberId);
    // then
    assertThat(adminTokenInfo.memberId()).isEqualTo(memberId);
    assertThat(adminTokenInfo.role()).isEqualTo(ADMIN);
  }
}