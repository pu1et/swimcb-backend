package com.project.swimcb.db.entity;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@Getter
@Table(name = "free_swimming")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class FreeSwimmingEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPoolEntity swimmingPool;

  @Column(name = "year_month", nullable = false)
  private LocalDate yearMonth;

  @Column(name = "days_of_week", nullable = false)
  private int daysOfWeek;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "lifeguard_id")
  private SwimmingInstructorEntity lifeguard;

  @Column(name = "capacity", nullable = false)
  private int capacity;

  @Column(name = "is_visible", nullable = false)
  private boolean isVisible;

  @Column(name = "is_canceled", nullable = false)
  private boolean isCanceled;

  @Column(name = "cancel_reason")
  private String cancelReason;

  @Column(name = "canceled_at")
  private LocalDateTime canceledAt;

  public void cancel(@NonNull String reason) {
    this.isCanceled = true;
    this.cancelReason = reason;
    this.canceledAt = LocalDateTime.now();
  }

}
