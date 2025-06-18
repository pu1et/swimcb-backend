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
@Table(name = "swimming_pool_image")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingPoolImageEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_pool_id", nullable = false)
  private SwimmingPoolEntity swimmingPool;

  @Getter
  @Column(name = "path", length = 255)
  private String path;

  public static SwimmingPoolImageEntity create(@NonNull SwimmingPoolEntity swimmingPool, @NonNull String path) {
    val image = new SwimmingPoolImageEntity();
    image.swimmingPool = swimmingPool;
    image.path = path;
    return image;
  }
}
