package com.project.swimcb.token.domain;

import static com.project.swimcb.token.domain.enums.MemberRole.ADMIN;
import static com.project.swimcb.token.domain.enums.MemberRole.CUSTOMER;
import static com.project.swimcb.token.domain.enums.MemberRole.GUEST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

  @DisplayName("fromToken 메서드 테스트")
  @Nested
  class FromToken {

    @Test
    @DisplayName("role이 GUEST이고 memberId가 null이면 정상적으로 TokenInfo를 반환해야한다.")
    void shouldThrowIllegalArgumentExceptionWhenRoleIsGuestAndMemberIdIsNull() {
      // given
      val role = GUEST;

      val decodedJWT = mock(DecodedJWT.class);
      when(decodedJWT.getSubject()).thenReturn(null);

      val roleClaim = mock(Claim.class);
      when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
      when(roleClaim.asString()).thenReturn(role.name());
      // when
      val tokenInfo = TokenInfo.fromToken(decodedJWT);
      // then
      assertThat(tokenInfo.memberId()).isNull();
      assertThat(tokenInfo.role()).isEqualTo(role);
    }

    @Test
    @DisplayName("role이 GUEST이고 memberId가 존재하면 IllegalArgumentException을 던진다.")
    void shouldThrowIllegalArgumentExceptionWhenRoleIsGuestAndMemberIdExists() {
      // given
      val memberId = 1L;
      val role = GUEST;

      val decodedJWT = mock(DecodedJWT.class);
      when(decodedJWT.getSubject()).thenReturn(String.valueOf(memberId));

      val roleClaim = mock(Claim.class);
      when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
      when(roleClaim.asString()).thenReturn(role.name());
      // when
      // then
      assertThrows(IllegalArgumentException.class, () -> TokenInfo.fromToken(decodedJWT));
    }

    @Test
    @DisplayName("role이 CUSTOMER이고 memberId가 존재하면 정상적으로 TokenInfo를 반환해야한다.")
    void shouldReturnTokenInfoWhenRoleIsCustomerAndMemberIdExists() {
      // given
      val memberId = 1L;
      val role = CUSTOMER;

      val decodedJWT = mock(DecodedJWT.class);
      when(decodedJWT.getSubject()).thenReturn(String.valueOf(memberId));

      val roleClaim = mock(Claim.class);
      when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
      when(roleClaim.asString()).thenReturn(role.name());
      // when
      val tokenInfo = TokenInfo.fromToken(decodedJWT);
      // then
      assertThat(tokenInfo.memberId()).isEqualTo(memberId);
      assertThat(tokenInfo.role()).isEqualTo(role);
    }

    @Test
    @DisplayName("role이 CUSTOMER이고 memberId가 null이면 IllegalArgumentException을 던진다.")
    void shouldThrowIllegalArgumentExceptionWhenRoleIsCustomerAndMemberIdIsNull() {
      // given
      val role = CUSTOMER;

      val decodedJWT = mock(DecodedJWT.class);
      when(decodedJWT.getSubject()).thenReturn(null);

      val roleClaim = mock(Claim.class);
      when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
      when(roleClaim.asString()).thenReturn(role.name());
      // when
      // then
      assertThrows(IllegalArgumentException.class, () -> TokenInfo.fromToken(decodedJWT));
    }

    @Test
    @DisplayName("role이 ADMIN이고 memberId가 존재하면 정상적으로 TokenInfo를 반환해야한다.")
    void shouldReturnTokenInfoWhenRoleIsAdminAndMemberIdExists() {
      // given
      val memberId = 1L;
      val role = ADMIN;

      val decodedJWT = mock(DecodedJWT.class);
      when(decodedJWT.getSubject()).thenReturn(String.valueOf(memberId));

      val roleClaim = mock(Claim.class);
      when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
      when(roleClaim.asString()).thenReturn(role.name());
      // when
      val tokenInfo = TokenInfo.fromToken(decodedJWT);
      // then
      assertThat(tokenInfo.memberId()).isEqualTo(memberId);
      assertThat(tokenInfo.role()).isEqualTo(role);
    }

    @Test
    @DisplayName("role이 ADMIN이고 memberId가 null이면 IllegalArgumentException을 던진다.")
    void shouldThrowIllegalArgumentExceptionWhenRoleIsAdminAndMemberIdIsNull() {
      // given
      val role = ADMIN;

      val decodedJWT = mock(DecodedJWT.class);
      when(decodedJWT.getSubject()).thenReturn(null);

      val roleClaim = mock(Claim.class);
      when(decodedJWT.getClaim("role")).thenReturn(roleClaim);
      when(roleClaim.asString()).thenReturn(role.name());
      // when
      // then
      assertThrows(IllegalArgumentException.class, () -> TokenInfo.fromToken(decodedJWT));
    }
  }

}