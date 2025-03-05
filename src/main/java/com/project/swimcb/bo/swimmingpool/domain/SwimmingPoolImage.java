package com.project.swimcb.bo.swimmingpool.domain;

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
@Table(name = "swimming_pool_image")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingPoolImage extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPool swimmingPool;

  @Getter
  @Column(name = "path", length = 255)
  private String path;

  public static SwimmingPoolImage create(@NonNull SwimmingPool swimmingPool, @NonNull String path) {
    val image = new SwimmingPoolImage();
    image.swimmingPool = swimmingPool;
    image.path = path;
    return image;
  }
}
