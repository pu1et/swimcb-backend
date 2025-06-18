package com.project.swimcb.db.entity;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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
public class SwimmingInstructorEntity extends BaseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPoolEntity swimmingPool;

  @Column(name = "name", length = 50)
  private String name;

  public static SwimmingInstructorEntity create(@NonNull SwimmingPoolEntity swimmingPool, @NonNull String name) {
    val swimmingInstructor = new SwimmingInstructorEntity();
    swimmingInstructor.swimmingPool = swimmingPool;
    swimmingInstructor.name = name;
    return swimmingInstructor;
  }

  public void updateName(@NonNull String name) {
    this.name = name;
  }
}
