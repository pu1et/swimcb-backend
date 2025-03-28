package com.project.swimcb.bo.instructor.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

@Getter
@Table(name = "swimming_instructor")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingInstructor extends BaseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPool swimmingPool;

  @Column(name = "name", length = 50)
  private String name;

  public static SwimmingInstructor create(@NonNull SwimmingPool swimmingPool, @NonNull String name) {
    val swimmingInstructor = new SwimmingInstructor();
    swimmingInstructor.swimmingPool = swimmingPool;
    swimmingInstructor.name = name;
    return swimmingInstructor;
  }

  public void updateName(@NonNull String name) {
    this.name = name;
  }
}
