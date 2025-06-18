package com.project.swimcb.db.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AccountNoConverter implements AttributeConverter<AccountNo, String> {

  @Override
  public String convertToDatabaseColumn(AccountNo attribute) {
    return attribute == null ? null : attribute.value();
  }

  @Override
  public AccountNo convertToEntityAttribute(String dbData) {
    return AccountNo.of(dbData);
  }
}
