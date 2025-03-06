package com.project.swimcb.bo.swimmingclass.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

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
@Table(name = "swimming_class_ticket")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingClassTicket extends BaseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "swimming_class_id", nullable = false)
  private SwimmingClass swimmingClass;

  @Column(name = "name", length = 20, nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private int price;

  public static SwimmingClassTicket create(@NonNull SwimmingClass swimmingClass,
      @NonNull String name, int price) {
    val swimmingClassTicket = new SwimmingClassTicket();
    swimmingClassTicket.swimmingClass = swimmingClass;
    swimmingClassTicket.name = name;
    swimmingClassTicket.price = price;
    return swimmingClassTicket;
  }
}
