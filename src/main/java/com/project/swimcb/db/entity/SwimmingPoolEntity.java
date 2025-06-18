package com.project.swimcb.db.entity;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

import com.project.swimcb.bo.swimmingpool.domain.UpdateSwimmingPoolBasicInfoCommand;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

@Getter
@Table(name = "swimming_pool")
@Entity
@NoArgsConstructor(access = PROTECTED)
public class SwimmingPoolEntity extends BaseEntity {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "admin_id", nullable = false)
  private AdminEntity adminEntity;

  @Column(name = "name", length = 100)
  private String name;

  @Column(name = "phone", length = 20)
  private String phone;

  @Column(name = "address", length = 255)
  private String address;

  @Column(name = "new_registration_period_start_day")
  private Integer newRegistrationPeriodStartDay;

  @Column(name = "new_registration_period_end_day")
  private Integer newRegistrationPeriodEndDay;

  @Column(name = "re_registration_period_start_day")
  private Integer reRegistrationPeriodStartDay;

  @Column(name = "re_registration_period_end_day")
  private Integer reRegistrationPeriodEndDay;

  @Column(name = "is_new_registration_extended", nullable = false)
  boolean isNewRegistrationExtended;

  @Column(name = "operating_days", length = 255)
  private String operatingDays;

  @Column(name = "closed_days", length = 255)
  private String closedDays;

  @Column(name = "usage_agreement_path", length = 255)
  private String usageAgreementPath;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  @Column(name = "account_no")
  @Convert(converter = AccountNoConverter.class)
  private AccountNo accountNo;

  public void updateBasicInfo(@NonNull UpdateSwimmingPoolBasicInfoCommand request) {
    this.name = request.name();
    this.phone = request.phone();
    this.address = request.address();
    this.newRegistrationPeriodStartDay = request.newRegistrationPeriodStartDay();
    this.newRegistrationPeriodEndDay = request.newRegistrationPeriodEndDay();
    this.reRegistrationPeriodStartDay = request.reRegistrationPeriodStartDay();
    this.reRegistrationPeriodEndDay = request.reRegistrationPeriodEndDay();
    this.isNewRegistrationExtended = request.isNewRegistrationExtended();
    this.operatingDays = request.operatingDays();
    this.closedDays = request.closedDays();
    this.accountNo = request.accountNo();
    this.usageAgreementPath = request.usageAgreementPath();
  }
}
