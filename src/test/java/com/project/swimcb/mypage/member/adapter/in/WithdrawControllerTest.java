package com.project.swimcb.mypage.member.adapter.in;

import static com.project.swimcb.mypage.member.adapter.in.WithdrawControllerTest.MEMBER_ID;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.mypage.member.application.port.in.WithdrawUseCase;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = WithdrawController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class WithdrawControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private WithdrawUseCase useCase;

  static final long MEMBER_ID = 1L;

  private static final String PATH = "/api/my-page/members/me";

  @Test
  @DisplayName("회원 탈퇴를 처리한다")
  void shouldWithdrawWhenAuthenticated() throws Exception {
    // given
    val memberId = 1L;

    // when
    // then
    mockMvc.perform(delete(PATH))
        .andExpect(status().isOk());

    then(useCase).should().withdraw(memberId);
  }

}
