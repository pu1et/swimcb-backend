package com.project.swimcb.favorite.domain;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record FreeSwimmingFavorite(

    @NonNull
    Long id,

    @NonNull
    Long targetId,

    @NonNull
    FavoriteTargetType targetType,

    String swimmingPoolName,

    @NonNull
    LocalDate date,

    @NonNull
    LocalTime startTime,

    @NonNull
    LocalTime endTime,

    @NonNull
    Integer minTicketPrice

) implements Favorite {

}
