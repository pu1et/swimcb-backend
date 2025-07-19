package com.project.swimcb.favorite.domain;

import com.project.swimcb.favorite.domain.enums.FavoriteTargetType;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import java.time.LocalTime;
import lombok.Builder;
import lombok.NonNull;

@Builder
public record SwimmingClassFavorite(

    @NonNull
    Long targetId,

    @NonNull
    FavoriteTargetType targetType,

    @NonNull
    String swimmingPoolName,

    @NonNull
    Integer month,

    @NonNull
    SwimmingClassTypeName type,

    @NonNull
    String subType,

    @NonNull
    ClassDayOfWeek daysOfWeek,

    @NonNull
    LocalTime startTime,

    @NonNull
    LocalTime endTime,

    @NonNull
    Integer minTicketPrice

) implements Favorite {

}
