package com.project.swimcb.bo.swimmingclass.domain;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.common.entity.BaseEntity;
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
public class SwimmingClassSubType extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_type_id", nullable = false)
  private SwimmingClassType swimmingClassType;

  @Column(name = "name", length = 20)
  private String name;

  public static SwimmingClassSubType create(@NonNull SwimmingClassType swimmingClassType,
      @NonNull String name) {

    val swimmingClassSubType = new SwimmingClassSubType();
    swimmingClassSubType.swimmingClassType = swimmingClassType;
    swimmingClassSubType.name = name;
    return swimmingClassSubType;
  }

  public void updateName(@NonNull String name) {
    this.name = name;
  }
}
