package com.project.swimcb.bo.reservation.adapter.in;

import com.project.swimcb.bo.reservation.domain.BoReservation;
import java.util.Optional;
import lombok.NonNull;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
public class FindBoReservationsResponseMapper {

  public FindBoReservationsResponse toResponse(@NonNull Page<BoReservation> reservations) {
    val result = reservations.getContent()
        .stream()
        .map(i ->
            FindBoReservationsResponse.BoReservation.builder()
                .member(FindBoReservationsResponse.Member.builder()
                    .id(i.member().id())
                    .name(i.member().name())
                    .birthDate(i.member().birthDate())
                    .build()
                )
                .swimmingClass(FindBoReservationsResponse.SwimmingClass.builder()
                    .id(i.swimmingClass().id())
                    .type(i.swimmingClass().type().getDescription())
                    .subType(i.swimmingClass().subType())
                    .days(i.swimmingClass().daysOfWeek().toDays())
                    .startTime(i.swimmingClass().startTime())
                    .endTime(i.swimmingClass().endTime())
                    .build()
                )
                .reservationDetail(FindBoReservationsResponse.ReservationDetail.builder()
                    .id(i.reservationDetail().id())
                    .ticketType(i.reservationDetail().ticketType())
                    .status(i.reservationDetail().status().getDescription())
                    .waitingNo(i.reservationDetail().waitingNo())
                    .reservedAt(i.reservationDetail().reservedAt())
                    .build())
                .payment(
                    FindBoReservationsResponse.Payment.builder()
                        .method(i.payment().method().getDescription())
                        .amount(i.payment().amount())
                        .pendingAt(i.payment().pendingAt())
                        .verificationAt(i.payment().verificationAt())
                        .approvedAt(i.payment().completedAt())
                        .build())
                .cancel(
                    Optional.ofNullable(i.cancel())
                        .map(j ->
                            FindBoReservationsResponse.Cancel.builder()
                                .canceledAt(j.canceledAt())
                                .reason(j.reason().getDescription())
                                .build()
                        )
                        .orElse(null)
                )
                .refund(
                    Optional.ofNullable(i.refund())
                        .map(j ->
                            FindBoReservationsResponse.Refund.builder()
                                .amount(j.amount())
                                .accountNo(j.accountNo().value())
                                .bankName(j.bankName())
                                .accountHolder(j.accountHolder())
                                .refundedAt(j.refundedAt())
                                .build()
                        )
                        .orElse(null)
                )
                .build()
        )
        .toList();
    return new FindBoReservationsResponse(
        new PageImpl<>(result, reservations.getPageable(), reservations.getTotalElements()));
  }
}
