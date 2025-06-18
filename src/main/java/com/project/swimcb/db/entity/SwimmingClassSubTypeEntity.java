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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

@Getter
@Table(name = "swimming_class_sub_type")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingClassSubTypeEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_type_id", nullable = false)
  private SwimmingClassTypeEntity swimmingClassType;

  @Column(name = "name", length = 20)
  private String name;

  public static SwimmingClassSubTypeEntity create(@NonNull SwimmingClassTypeEntity swimmingClassType,
      @NonNull String name) {

    val swimmingClassSubType = new SwimmingClassSubTypeEntity();
    swimmingClassSubType.swimmingClassType = swimmingClassType;
    swimmingClassSubType.name = name;
    return swimmingClassSubType;
  }

  public void updateName(@NonNull String name) {
    this.name = name;
  }
}
