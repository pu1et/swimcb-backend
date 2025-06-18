package com.project.swimcb.db.entity;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Table(name = "favorite")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class FavoriteEntity extends BaseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  private MemberEntity member;

  @Column(name = "target_id", nullable = false)
  private long targetId;

  @Enumerated(STRING)
  @Column(name = "target_type", length = 20, nullable = false)
  private FavoriteTargetType targetType;
}
