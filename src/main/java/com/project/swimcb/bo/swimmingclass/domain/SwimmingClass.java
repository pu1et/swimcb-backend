package com.project.swimcb.bo.swimmingclass.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.bo.instructor.domain.SwimmingInstructor;
import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "swimming_class")
@Entity
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PROTECTED)
public class SwimmingClass extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPool swimmingPool;

  @Column(name = "month", nullable = false)
  private int month;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_type_id", nullable = false)
  private SwimmingClassType type;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_sub_type_id", nullable = false)
  private SwimmingClassSubType subType;

  @Column(name = "is_monday", nullable = false)
  private boolean isMonday;

  @Column(name = "is_tuesday", nullable = false)
  private boolean isTuesday;

  @Column(name = "is_wednesday", nullable = false)
  private boolean isWednesday;

  @Column(name = "is_thursday", nullable = false)
  private boolean isThursday;

  @Column(name = "is_friday", nullable = false)
  private boolean isFriday;

  @Column(name = "is_saturday", nullable = false)
  private boolean isSaturday;

  @Column(name = "is_sunday", nullable = false)
  private boolean isSunday;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;

  @OneToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_instructor_id", nullable = false)
  private SwimmingInstructor instructor;

  @Column(name = "total_capacity", nullable = false)
  private int totalCapacity;

  @Column(name = "reservation_limit_count", nullable = false)
  private int reservationLimitCount;

  @Column(name = "is_visible", nullable = false)
  private boolean isVisible;
}
