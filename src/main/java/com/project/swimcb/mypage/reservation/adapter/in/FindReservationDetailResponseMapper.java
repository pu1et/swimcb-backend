package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.domain.ReservationDetail;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservationDetailResponseMapper {

  private final ImageUrlPort imageUrlPort;

  public FindReservationDetailResponse toResponse(@NonNull ReservationDetail detail) {
    val imageUrl = imageUrlPort.getImageUrl(detail.swimmingPool().imagePath());

    return FindReservationDetailResponse.builder()
        .swimmingPool(
            FindReservationDetailResponse.SwimmingPool.builder()
                .id(detail.swimmingPool().id())
                .name(detail.swimmingPool().name())
                .phone(detail.swimmingPool().phone())
                .imageUrl(imageUrl)
                .accountNo(detail.swimmingPool().accountNo().value())
                .build()
        )
        .swimmingClass(
            FindReservationDetailResponse.SwimmingClass.builder()
                .id(detail.swimmingClass().id())
                .month(detail.swimmingClass().month())
                .type(detail.swimmingClass().type().getDescription())
                .subType(detail.swimmingClass().subType())
                .days(detail.swimmingClass().daysOfWeek().toDays())
                .startTime(detail.swimmingClass().startTime())
                .endTime(detail.swimmingClass().endTime())
                .build()
        )
        .ticket(
            FindReservationDetailResponse.Ticket.builder()
                .id(detail.ticket().id())
                .name(detail.ticket().name())
                .price(detail.ticket().price())
                .build()
        )
        .reservation(
            FindReservationDetailResponse.Reservation.builder()
                .id(detail.reservation().id())
                .status(detail.reservation().status().getDescription())
                .reservedAt(detail.reservation().reservedAt())
                .waitingNo(detail.reservation().waitingNo())
                .build()
        )
        .payment(
            FindReservationDetailResponse.Payment.builder()
                .method(detail.payment().method().getDescription())
                .amount(detail.payment().amount())
                .pendingAt(detail.payment().pendingAt())
                .approvedAt(detail.payment().approvedAt())
                .build()
        )
        .cancel(
            FindReservationDetailResponse.Cancel.builder()
                .canceledAt(detail.cancel().canceledAt())
                .build()
        )
        .refund(
            FindReservationDetailResponse.Refund.builder()
                .amount(detail.refund().amount())
                .accountNo(accountNo(detail.refund().accountNo()))
                .bankName(detail.refund().bankName())
                .refundedAt(detail.refund().refundedAt())
                .build()
        )
        .review(
            FindReservationDetailResponse.Review.builder()
                .id(detail.review().id())
                .build()
        )
        .build();
  }

  private String accountNo(AccountNo accountNo) {
    if (accountNo == null) {
      return null;
    }
    return accountNo.value();
  }
}
