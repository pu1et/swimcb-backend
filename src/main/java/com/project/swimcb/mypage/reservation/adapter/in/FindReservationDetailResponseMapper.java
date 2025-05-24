package com.project.swimcb.mypage.reservation.adapter.in;

import static com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.Payment;
import static com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.Reservation;
import static com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.SwimmingClass;
import static com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.SwimmingPool;
import static com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.Ticket;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.bo.swimmingpool.domain.AccountNo;
import com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.Cancel;
import com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.Refund;
import com.project.swimcb.mypage.reservation.adapter.in.FindReservationDetailResponse.Review;
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
            SwimmingPool.builder()
                .id(detail.swimmingPool().id())
                .name(detail.swimmingPool().name())
                .phone(detail.swimmingPool().phone())
                .imageUrl(imageUrl)
                .accountNo(detail.swimmingPool().accountNo().value())
                .build()
        )
        .swimmingClass(
            SwimmingClass.builder()
                .id(detail.swimmingClass().id())
                .month(detail.swimmingClass().month())
                .type(detail.swimmingClass().type().getDescription())
                .subType(detail.swimmingClass().subType())
                .days(detail.swimmingClass().daysOfWeek().toDays())
                .startTime(detail.swimmingClass().startTime())
                .endTime(detail.swimmingClass().endTime())
                .isCanceled(detail.swimmingClass().isCanceled())
                .cancelledReason(detail.swimmingClass().cancelledReason())
                .build()
        )
        .ticket(
            Ticket.builder()
                .id(detail.ticket().id())
                .name(detail.ticket().name())
                .price(detail.ticket().price())
                .build()
        )
        .reservation(
            Reservation.builder()
                .id(detail.reservation().id())
                .status(detail.reservation().status().getDescription())
                .reservedAt(detail.reservation().reservedAt())
                .waitingNo(detail.reservation().waitingNo())
                .build()
        )
        .payment(
            Payment.builder()
                .method(detail.payment().method().getDescription())
                .amount(detail.payment().amount())
                .pendingAt(detail.payment().pendingAt())
                .approvedAt(detail.payment().approvedAt())
                .build()
        )
        .cancel(cancel(detail))
        .refund(refund(detail))
        .review(review(detail))
        .build();
  }

  private Cancel cancel(@NonNull ReservationDetail detail) {
    if (detail.cancel() == null) {
      return null;
    }
    return Cancel.builder()
        .canceledAt(detail.cancel().canceledAt())
        .build();
  }

  private Refund refund(@NonNull ReservationDetail detail) {
    if (detail.refund() == null) {
      return null;
    }
    return Refund.builder()
        .amount(detail.refund().amount())
        .accountNo(accountNo(detail.refund().accountNo()))
        .bankName(detail.refund().bankName())
        .refundedAt(detail.refund().refundedAt())
        .build();
  }

  private Review review(@NonNull ReservationDetail detail) {
    if (detail.review() == null) {
      return null;
    }
    return FindReservationDetailResponse.Review.builder()
        .id(detail.review().id())
        .build();
  }

  private String accountNo(AccountNo accountNo) {
    if (accountNo == null) {
      return null;
    }
    return accountNo.value();
  }
}
