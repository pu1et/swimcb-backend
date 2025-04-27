package com.project.swimcb.bo.reservation.domain;

import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import java.time.LocalDate;
import lombok.Builder;
import lombok.NonNull;
import org.springframework.data.domain.Pageable;

@Builder
public record FindBoReservationsCondition(
    @NonNull Long swimmingPoolId,
    @NonNull LocalDate startDate,
    @NonNull LocalDate endDate,
    TicketType programType,
    ReservationStatus reservationStatus,
    PaymentMethod paymentMethod,
    @NonNull Pageable pageable
) {

}
