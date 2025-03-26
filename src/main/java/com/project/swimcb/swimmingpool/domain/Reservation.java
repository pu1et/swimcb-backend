package com.project.swimcb.swimmingpool.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.bo.reservation.domain.enums.PaymentMethod;
import com.project.swimcb.common.entity.BaseEntity;
import com.project.swimcb.member.domain.Member;
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
import lombok.NoArgsConstructor;

@Table(name = "reservation")
@Entity
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
}
