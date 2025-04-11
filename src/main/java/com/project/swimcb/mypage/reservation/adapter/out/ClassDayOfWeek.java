package com.project.swimcb.mypage.reservation.adapter.out;

import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.SATURDAY;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import lombok.NonNull;
import lombok.val;

public record ClassDayOfWeek(@NonNull List<DayOfWeek> value) {

  public static ClassDayOfWeek of(int dayOfWeek) {
    val days = IntStream.range(0, 7)
        .filter(i -> (dayOfWeek & (1 << (6 - i))) != 0)
        .mapToObj(i -> DayOfWeek.of(i + 1))
        .toList();
    return new ClassDayOfWeek(days);
  }

  public List<String> toDays() {
    val dayMap = Map.of(
        MONDAY, "월",
        TUESDAY, "화",
        WEDNESDAY, "수",
        THURSDAY, "목",
        FRIDAY, "금",
        SATURDAY, "토",
        SUNDAY, "일"
    );

    return value.stream()
        .map(dayMap::get)
        .toList();
  }
}
