package com.project.swimcb.db.entity;

import java.util.Optional;

public record AccountNo(String value) {

  public AccountNo {
    value = Optional.ofNullable(value)
        .map(v -> v.replace("-", "").trim())
        .orElse(null);
  }

  public static AccountNo of(String value) {
    return value == null ? null : new AccountNo(value);
  }
}
