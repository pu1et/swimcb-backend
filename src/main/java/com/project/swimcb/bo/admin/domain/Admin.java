package com.project.swimcb.bo.admin.domain;

import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;

@Table(name = "admin")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class Admin extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "phone", length = 20, nullable = false)
  private String phone;

  @Column(name = "login_id", length = 100, nullable = false)
  private String loginId;

  @Column(name = "password", length = 40, nullable = false)
  private String password;
}
