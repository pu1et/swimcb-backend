package com.project.swimcb.swimmingpool.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailMainGateway;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolDetailMainInteractorTest {

  @InjectMocks
  private FindSwimmingPoolDetailMainInteractor interactor;

  @Mock
  private FindSwimmingPoolDetailMainGateway gateway;

  private static final long SWIMMING_POOL_ID = 1L;

  @Test
  @DisplayName("수영장 ID와 회원 ID로 수영장 메인 정보를 조회한다.")
  void findSwimmingPoolDetailMain() {
    // given
    val memberId = 2L;
    // when
    interactor.findSwimmingPoolDetailMain(SWIMMING_POOL_ID, memberId);
    // then
    verify(gateway, only()).findSwimmingPoolDetailMain(SWIMMING_POOL_ID, memberId);
  }

  @Test
  @DisplayName("비회원 조회시 회원 ID가 null이어도 수영장 메인 정보를 조회한다.")
  void findSwimmingPoolDetailMainWithNullMemberId() {
    // given
    val swimmingPoolId = 1L;
    // when
    interactor.findSwimmingPoolDetailMain(swimmingPoolId, null);
    // then
    verify(gateway, only()).findSwimmingPoolDetailMain(swimmingPoolId, null);
  }
}