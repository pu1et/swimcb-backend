package com.project.swimcb.reservation.adapter.in;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import com.project.swimcb.reservation.domain.ReservationInfo;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class FindReservationResponseMapper {

  public FindReservationResponse toResponse(@NonNull ReservationInfo reservationInfo) {
    return FindReservationResponse.builder()
        .swimmingPool(
            FindReservationResponse.SwimmingPool.builder()
                .id(reservationInfo.swimmingPool().id())
                .name(reservationInfo.swimmingPool().name())
                .accountNo(reservationInfo.swimmingPool().accountNo().value())
                .build()
        )
        .swimmingClass(
            FindReservationResponse.SwimmingClass.builder()
                .id(reservationInfo.swimmingClass().id())
                .month(reservationInfo.swimmingClass().month())
                .type(reservationInfo.swimmingClass().type().getDescription())
                .subType(reservationInfo.swimmingClass().subType())
                .days(bitVectorToDays(reservationInfo.swimmingClass().daysOfWeek()))
                .startTime(reservationInfo.swimmingClass().startTime())
                .endTime(reservationInfo.swimmingClass().endTime())
                .build()
        )
        .ticket(
            FindReservationResponse.Ticket.builder()
                .id(reservationInfo.ticket().id())
                .name(reservationInfo.ticket().name())
                .price(reservationInfo.ticket().price())
                .build()
        )
        .reservation(
            FindReservationResponse.Reservation.builder()
                .id(reservationInfo.reservation().id())
                .reservedAt(reservationInfo.reservation().reservedAt())
                .waitingNo(reservationInfo.reservation().waitingNo())
                .build()
        )
        .payment(
            FindReservationResponse.Payment.builder()
                .method(reservationInfo.payment().method().getDescription())
                .build()
        )
        .build();
  }

  List<String> bitVectorToDays(int bitVector) {
    val dayMap = Map.of(
        MONDAY, "월",
        TUESDAY, "화",
        WEDNESDAY, "수",
        THURSDAY, "목",
        FRIDAY, "금",
        SATURDAY, "토",
        SUNDAY, "일"
    );

    return IntStream.range(0, 7)
        .filter(i -> (bitVector & (1 << (6 - i))) > 0)
        .mapToObj(i -> dayMap.get(DayOfWeek.of(i + 1)))
        .toList();
  }
}
