package com.project.swimcb.swimmingpool.domain;

import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.TicketType.SWIMMING_CLASS;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.common.entity.BaseEntity;
import com.project.swimcb.member.domain.Member;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Table(name = "reservation")
@Entity
@Builder(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class Reservation extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @Enumerated(STRING)
  @Column(name = "ticket_type", nullable = false)
  private TicketType ticketType;

  @Column(name = "ticket_id", nullable = false)
  private long ticketId;

  @Enumerated(STRING)
  @Column(name = "payment_method", nullable = false)
  private PaymentMethod paymentMethod;

  @Column(name = "reserved_at", nullable = false)
  private LocalDateTime reservedAt;

  @Enumerated(STRING)
  @Column(name = "reservation_status", nullable = false)
  private ReservationStatus reservationStatus;

  @Column(name = "waiting_no")
  private Integer waitingNo;

  @Builder(builderClassName = "createClassReservation", builderMethodName = "createClassReservation")
  private static Reservation create(@NonNull Member member, long ticketId,
      @NonNull PaymentMethod paymentMethod) {

    return Reservation.builder()
        .member(member)
        .ticketType(SWIMMING_CLASS)
        .ticketId(ticketId)
        .paymentMethod(paymentMethod)
        .reservedAt(LocalDateTime.now())
        .reservationStatus(PAYMENT_PENDING)
        .build();
  }
}


