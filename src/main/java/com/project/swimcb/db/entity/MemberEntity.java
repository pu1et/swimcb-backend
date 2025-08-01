package com.project.swimcb.db.entity;

import static jakarta.persistence.EnumType.STRING;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "phone", length = 20, nullable = false)
  private String phone;

  @Enumerated(STRING)
  @Column(name = "gender", length = 10)
  private Gender gender;

  @Column(name = "email", length = 255, nullable = false, unique = true)
  private String email;

  @Column(name = "nickname", length = 100)
  private String nickname;

  @Builder(toBuilder = true)
  public MemberEntity(
      @NonNull String name,
      @NonNull String phone,
      @NonNull String email,
      @NonNull String nickname
  ) {
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.nickname = nickname;
  }

  public MemberEntity(long id) {
    this.id = id;
  }

}
