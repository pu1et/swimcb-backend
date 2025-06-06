package com.project.swimcb.bo.swimmingclass.domain;

import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.bo.swimmingpool.domain.SwimmingPool;
import com.project.swimcb.common.entity.BaseEntity;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "swimming_class_type")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingClassType extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPool swimmingPool;

  @Enumerated(STRING)
  @Column(name = "name", length = 20)
  private SwimmingClassTypeName name;
}
