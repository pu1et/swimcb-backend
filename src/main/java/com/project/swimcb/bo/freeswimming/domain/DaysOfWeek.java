package com.project.swimcb.bo.freeswimming.domain;

import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.val;

public record DaysOfWeek(@NonNull List<DayOfWeek> value) {

  public int toInt() {
    return value
        .stream()
        .map(i -> 1 << (6 - (i.getValue() - 1))).reduce(0, Integer::sum);
  }

  public static DaysOfWeek of(@NonNull Integer daysOfWeek) {
    val value = IntStream.range(0, 7)
        .filter(i -> (daysOfWeek & (1 << (6 - i))) != 0)
        .mapToObj(i -> DayOfWeek.of(i + 1))
        .toList();
    return new DaysOfWeek(value);
  }

}
