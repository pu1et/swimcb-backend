package com.project.swimcb.mypage.member.adapter.in;

import static com.project.swimcb.mypage.member.adapter.in.FindMemberInfoControllerTest.MEMBER_ID;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.mypage.member.application.port.in.FindMemberInfoUseCase;
import com.project.swimcb.mypage.member.domain.MemberInfo;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindMemberInfoController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindMemberInfoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindMemberInfoUseCase useCase;

  static final long MEMBER_ID = 1L;

  private static final String PATH = "/api/my-page/members/me";

  @Test
  @DisplayName("회원 정보를 조회한다")
  void shouldReturnMemberInfoWhenAuthenticated() throws Exception {
    // given
    val memberId = 1L;
    val memberInfo = MemberInfo.builder()
        .name("홍길동")
        .phone("010-1234-5678")
        .email("test@example.com")
        .build();

    val response = FindMemberInfoResponse.builder()
        .name(memberInfo.name())
        .phone(memberInfo.phone())
        .email(memberInfo.email())
        .build();

    given(useCase.findMemberInfo(memberId)).willReturn(memberInfo);

    // when
    // then
    mockMvc.perform(get(PATH))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    then(useCase).should().findMemberInfo(memberId);
  }

}
