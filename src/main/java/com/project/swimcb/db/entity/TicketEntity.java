package com.project.swimcb.db.entity;

import static com.project.swimcb.db.entity.TicketTargetType.*;
import static jakarta.persistence.EnumType.*;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

@Getter
@Table(name = "ticket")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class TicketEntity extends BaseEntity {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @Enumerated(STRING)
  @Column(name = "target_type", nullable = false)
  private TicketTargetType targetType;

  @Column(name = "target_id", nullable = false)
  private long targetId;

  @Column(name = "name", length = 20, nullable = false)
  private String name;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "is_deleted", nullable = false)
  private boolean isDeleted;

  public static TicketEntity create(@NonNull SwimmingClassEntity swimmingClass,
      @NonNull String name, int price) {
    val swimmingClassTicket = new TicketEntity();
    swimmingClassTicket.targetType = SWIMMING_CLASS;
    swimmingClassTicket.targetId = swimmingClass.getId();
    swimmingClassTicket.name = name;
    swimmingClassTicket.price = price;
    swimmingClassTicket.isDeleted = false;
    return swimmingClassTicket;
  }

}
