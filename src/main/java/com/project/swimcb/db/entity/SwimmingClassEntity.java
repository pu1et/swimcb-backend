package com.project.swimcb.db.entity;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.swimmingpool.domain.SwimmingClassAvailabilityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@Table(name = "swimming_class")
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class SwimmingClassEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPoolEntity swimmingPool;

  @Column(name = "year", nullable = false)
  private int year;

  @Column(name = "month", nullable = false)
  private int month;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_type_id", nullable = false)
  private SwimmingClassTypeEntity type;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_sub_type_id", nullable = false)
  private SwimmingClassSubTypeEntity subType;

  @Column(name = "days_of_week", nullable = false)
  private int daysOfWeek;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_instructor_id", nullable = false)
  private SwimmingInstructorEntity instructor;

  @Column(name = "total_capacity", nullable = false)
  private int totalCapacity;

  @Column(name = "reservation_limit_count", nullable = false)
  private int reservationLimitCount;

  @Column(name = "reserved_count", nullable = false)
  private int reservedCount;

  @Column(name = "is_visible", nullable = false)
  private boolean isVisible;

  @Column(name = "is_canceled", nullable = false)
  private boolean isCanceled;

  @Column(name = "cancel_reason")
  private String cancelReason;

  @Column(name = "canceled_at")
  private LocalDateTime canceledAt;

  public SwimmingClassAvailabilityStatus getReservationStatus() {
    return SwimmingClassAvailabilityStatus.calculateStatus(this.reservationLimitCount,
        this.reservedCount);
  }

  public void increaseReservedCount() {
    this.reservedCount++;
  }

  public int calculateWaitingNum() {
    return this.reservedCount - this.reservationLimitCount + 1;
  }

  public void cancel(@NonNull String cancelReason) {
    this.isCanceled = true;
    this.cancelReason = cancelReason;
    this.canceledAt = LocalDateTime.now();
  }

}
