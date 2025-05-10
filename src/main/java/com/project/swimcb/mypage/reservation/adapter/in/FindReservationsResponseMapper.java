package com.project.swimcb.mypage.reservation.adapter.in;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.mypage.reservation.domain.Reservation;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindReservationsResponseMapper {

  private final ImageUrlPort imageUrlPort;

  public FindReservationsResponse toResponse(@NonNull Page<Reservation> reservations) {
    val result = reservations.getContent()
        .stream()
        .map(i ->
            FindReservationsResponse.Reservation.builder()
                .swimmingPool(FindReservationsResponse.SwimmingPool.builder()
                    .id(i.swimmingPool().id())
                    .name(i.swimmingPool().name())
                    .imageUrl(imageUrl(i.swimmingPool().imagePath()))
                    .build()
                )
                .swimmingClass(FindReservationsResponse.SwimmingClass.builder()
                    .id(i.swimmingClass().id())
                    .month(i.swimmingClass().month())
                    .type(i.swimmingClass().type().getDescription())
                    .subType(i.swimmingClass().subType())
                    .days(i.swimmingClass().daysOfWeek().toDays())
                    .startTime(i.swimmingClass().startTime())
                    .endTime(i.swimmingClass().endTime())
                    .isCanceled(i.swimmingClass().isCanceled())
                    .build()
                )
                .ticket(FindReservationsResponse.Ticket.builder()
                    .id(i.ticket().id())
                    .name(i.ticket().name())
                    .price(i.ticket().price())
                    .build()
                )
                .reservationInfo(FindReservationsResponse.ReservationInfo.builder()
                    .id(i.reservationDetail().id())
                    .ticketType(i.reservationDetail().ticketType())
                    .status(i.reservationDetail().status().getDescription())
                    .reservedAt(i.reservationDetail().reservedAt())
                    .waitingNo(i.reservationDetail().waitingNo())
                    .build()
                )
                .review(
                    Optional.ofNullable(i.review())
                        .map(
                            r -> FindReservationsResponse.Review.builder()
                                .id(r.id())
                                .build()
                        )
                        .orElse(null)
                )
                .build()
        )
        .toList();
    return new FindReservationsResponse(
        new PageImpl<>(result, reservations.getPageable(), reservations.getTotalElements()));
  }

  private @NonNull String imageUrl(@NonNull String imagePath) {
    return imageUrlPort.getImageUrl(imagePath);
  }
}
