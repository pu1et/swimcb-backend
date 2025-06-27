package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolImageEntity.swimmingPoolImageEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolRatingEntity.swimmingPoolRatingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolReviewEntity.swimmingPoolReviewEntity;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static com.querydsl.core.types.Projections.constructor;
import static com.querydsl.core.types.Projections.list;

import com.project.swimcb.swimmingpool.application.out.FindSwimmingPoolDetailMainGateway;
import com.project.swimcb.swimmingpool.domain.SwimmingPoolDetailMain;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FindSwimmingPoolDetailMainDataMapper implements FindSwimmingPoolDetailMainGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public SwimmingPoolDetailMain findSwimmingPoolDetailMain(long swimmingPoolId, Long memberId) {
    val pool = queryFactory.select(constructor(SwimmingPool.class,
            list(swimmingPoolImageEntity.path),
            swimmingPoolEntity.name,
            favoriteEntity.id.count().gt(0),
            swimmingPoolRatingEntity.rating.avg(),
            swimmingPoolReviewEntity.id.sum(),
            swimmingPoolEntity.address,
            swimmingPoolEntity.phone,
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.longitude
        ))
        .from(swimmingPoolEntity)
        .join(swimmingPoolImageEntity)
        .on(swimmingPoolImageEntity.swimmingPool.eq(swimmingPoolEntity))
        .leftJoin(favoriteEntity).on(favoriteJoinIfMemberIdExist(memberId))
        .leftJoin(swimmingPoolRatingEntity)
        .on(swimmingPoolRatingEntity.swimmingPool.eq(swimmingPoolEntity))
        .leftJoin(swimmingPoolReviewEntity)
        .on(swimmingPoolReviewEntity.swimmingPool.eq(swimmingPoolEntity))
        .where(swimmingPoolEntity.id.eq(swimmingPoolId))
        .groupBy(
            swimmingPoolEntity.name,
            swimmingPoolEntity.address,
            swimmingPoolEntity.phone,
            swimmingPoolImageEntity.path,
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.longitude
        )
        .fetchFirst();

    if (pool == null) {
      throw new NoSuchElementException("수영장이 존재하지 않습니다 : " + swimmingPoolId);
    }

    return SwimmingPoolDetailMain.builder()
        .imagePaths(pool.imagePaths())
        .name(pool.name())
        .isFavorite(pool.isFavorite())
        .rating(pool.rating())
        .reviewCount((int) pool.reviewCount())
        .address(pool.address())
        .phone(pool.phone())
        .latitude(pool.latitude())
        .longitude(pool.longitude())
        .build();
  }

  BooleanExpression favoriteJoinIfMemberIdExist(Long memberId) {
    if (memberId == null) {
      return Expressions.FALSE;
    }
    return favoriteEntity.member.id.eq(memberId)
        .and(favoriteEntity.targetId.eq(swimmingPoolEntity.id))
        .and(favoriteEntity.targetType.eq(SWIMMING_POOL));
  }

  public record SwimmingPool(
      @NonNull List<String> imagePaths,
      @NonNull String name,
      boolean isFavorite,
      double rating,
      long reviewCount,
      @NonNull String address,
      @NonNull String phone,
      Double latitude,
      Double longitude
  ) {

  }

}
