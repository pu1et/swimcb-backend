package com.project.swimcb.bo.reservation.application.port.in;

import lombok.NonNull;

public interface AutoCancelReservationsUseCase {

  /**
   * 수영장별 결제대기 시간(24시간)이 만료된 예약 자동 취소 처리
   */
  void cancelPaymentExpiredReservationsBySwimmingPoolId(@NonNull Long swimmingPoolId);

  /**
   * 회원별 결제대기 시간(24시간)이 만료된 예약 자동 취소 처리
   */
  void cancelPaymentExpiredReservationsByMemberId(@NonNull Long memberId);

}
