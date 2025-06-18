package com.project.swimcb.db.entity;

import static jakarta.persistence.EnumType.*;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.db.entity.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(name = "member")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class MemberEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @Column(name = "name", length = 100, nullable = false)
  private String name;

  @Column(name = "birth_date", nullable = false)
  private LocalDate birthDate;

  @Column(name = "phone", length = 20, nullable = false)
  private String phone;

  @Enumerated(STRING)
  @Column(name = "gender", length = 10, nullable = false)
  private Gender gender;

  @Column(name = "email", length = 255)
  private String email;

  @Column(name = "nickname", length = 100)
  private String nickname;
}
