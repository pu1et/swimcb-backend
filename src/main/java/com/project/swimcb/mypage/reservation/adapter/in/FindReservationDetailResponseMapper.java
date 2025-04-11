package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
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
                .imageUrl(imageUrl)
                .build()
        )
        .swimmingClass(
            FindReservationDetailResponse.SwimmingClass.builder()
                .id(detail.swimmingClass().id())
                .type(detail.swimmingClass().type().getDescription())
                .subType(detail.swimmingClass().subType())
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
                .build()
        )
        .payment(
            FindReservationDetailResponse.Payment.builder()
                .method(detail.payment().method().getDescription())
                .amount(detail.payment().amount())
                .requestedAt(detail.reservation().reservedAt())
                .build()
        )
        .build();
  }
}
