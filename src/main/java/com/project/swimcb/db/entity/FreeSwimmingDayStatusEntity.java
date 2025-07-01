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
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Table(name = "free_swimming_day_status")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class FreeSwimmingDayStatusEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "free_swimming_id", nullable = false)
  private FreeSwimmingEntity freeSwimming;

  @Column(name = "day_of_month", nullable = false)
  private int dayOfMonth;

  @Column(name = "reserved_count", nullable = false)
  private int reservedCount;

  @Column(name = "is_closed", nullable = false)
  private boolean isClosed;

  @Column(name = "is_reservation_blocked", nullable = false)
  private boolean isReservationBlocked;

}
