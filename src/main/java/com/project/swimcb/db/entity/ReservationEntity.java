package com.project.swimcb.db.entity;

import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_VERIFICATION;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.REFUND_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_CANCELLED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.TicketType.SWIMMING_CLASS;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import com.project.swimcb.swimmingpool.domain.enums.PaymentMethod;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import lombok.val;

@Getter
@Table(name = "reservation")
@Entity
@Builder(access = PRIVATE)
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class ReservationEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity member;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_id")
  private SwimmingClassEntity swimmingClass;

  @Enumerated(STRING)
  @Column(name = "ticket_type", length = 20, nullable = false)
  private TicketType ticketType;

  @Column(name = "ticket_id", nullable = false)
  private long ticketId;

  @Column(name = "reserved_at", nullable = false)
  private LocalDateTime reservedAt;

  @Enumerated(STRING)
  @Column(name = "reservation_status", length = 30, nullable = false)
  private ReservationStatus reservationStatus;

  @Column(name = "payment_pending_at")
  private LocalDateTime paymentPendingAt;

  @Enumerated(STRING)
  @Column(name = "payment_method", length = 20, nullable = false)
  private PaymentMethod paymentMethod;

  @Column(name = "payment_amount", nullable = false)
  private int paymentAmount;

  @Column(name = "payment_verification_at")
  private LocalDateTime paymentVerificationAt;

  @Column(name = "payment_approved_at")
  private LocalDateTime paymentApprovedAt;

  @Enumerated(STRING)
  @Column(name = "cancellation_reason", length = 50)
  private CancellationReason cancellationReason;

  @Column(name = "canceled_at")
  private LocalDateTime canceledAt;

  @Column(name = "refund_amount")
  private Integer refundAmount;

  @Column(name = "refund_account_no", length = 50)
  @Convert(converter = AccountNoConverter.class)
  private AccountNo refundAccountNo;

  @Column(name = "refund_bank_name", length = 50)
  private String refundBankName;

  @Column(name = "refund_account_holder", length = 100)
  private String refundAccountHolder;

  @Column(name = "refunded_at")
  private LocalDateTime refundedAt;

  @Builder(builderClassName = "createClassNormalReservation", builderMethodName = "createClassNormalReservation")
  private static ReservationEntity createClassNormal(@NonNull MemberEntity member,
      @NonNull SwimmingClassEntity swimmingClass, @NonNull Long ticketId,
      @NonNull PaymentMethod paymentMethod,
      int paymentAmount) {

    val now = LocalDateTime.now();

    return ReservationEntity.builder()
        .member(member)
        .swimmingClass(swimmingClass)
        .ticketType(SWIMMING_CLASS)
        .ticketId(ticketId)
        .paymentMethod(paymentMethod)
        .reservedAt(now)
        .reservationStatus(PAYMENT_PENDING)
        .paymentPendingAt(now)
        .paymentAmount(paymentAmount)
        .build();
  }

  @Builder(builderClassName = "createClassWaitingReservation", builderMethodName = "createClassWaitingReservation")
  private static ReservationEntity createClassWaiting(@NonNull MemberEntity member,
      @NonNull SwimmingClassEntity swimmingClass, @NonNull Long ticketId,
      @NonNull PaymentMethod paymentMethod, int paymentAmount) {

    return ReservationEntity.builder()
        .member(member)
        .swimmingClass(swimmingClass)
        .ticketType(SWIMMING_CLASS)
        .ticketId(ticketId)
        .paymentMethod(paymentMethod)
        .reservedAt(LocalDateTime.now())
        .reservationStatus(RESERVATION_PENDING)
        .paymentAmount(paymentAmount)
        .build();
  }

  public void cancel(@NonNull CancellationReason reason) {
    this.reservationStatus = RESERVATION_CANCELLED;
    this.canceledAt = LocalDateTime.now();
    this.cancellationReason = reason;
  }

  public boolean canTransitionToComplete() {
    return this.reservationStatus == PAYMENT_PENDING ||
        this.reservationStatus == PAYMENT_VERIFICATION;
  }

  public void complete(@NonNull PaymentMethod paymentMethod) {
    this.reservationStatus = PAYMENT_COMPLETED;
    this.paymentApprovedAt = LocalDateTime.now();
    this.paymentMethod = paymentMethod;
  }

  public boolean canTransitionToCancelByAdmin() {
    return this.reservationStatus == PAYMENT_VERIFICATION;
  }

  public boolean canTransitionToCancelByUser() {
    return this.reservationStatus == RESERVATION_PENDING ||
        this.reservationStatus == PAYMENT_PENDING;
  }

  public boolean canTransitionToPaymentVerificationByUser() {
    return this.reservationStatus == PAYMENT_PENDING;
  }

  public void updateStatusToPaymentVerification() {
    this.reservationStatus = PAYMENT_VERIFICATION;
    this.paymentVerificationAt = LocalDateTime.now();
  }

  // 환불가능한 상태
  // 결제완료 : 정상 플로우
  // 취소완료 : 입금했는데 입금확인중을 누르지 않아 자동 취소된 경우
  public boolean canTransitionToRefund() {
    return this.reservationStatus == PAYMENT_COMPLETED
        || this.reservationStatus == RESERVATION_CANCELLED;
  }

  public void refund(
      @NonNull String bankName,
      @NonNull AccountNo accountNo,
      @NonNull String accountHolder,
      @NonNull Integer amount
  ) {
    this.reservationStatus = REFUND_COMPLETED;
    this.refundBankName = bankName;
    this.refundAccountNo = accountNo;
    this.refundAccountHolder = accountHolder;
    this.refundAmount = amount;
    this.refundedAt = LocalDateTime.now();
  }

  public boolean canChangePaymentMethod() {
    return this.reservationStatus == RESERVATION_PENDING
        || this.reservationStatus == PAYMENT_PENDING;
  }

  public void changePaymentMethod(@NonNull PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

}


