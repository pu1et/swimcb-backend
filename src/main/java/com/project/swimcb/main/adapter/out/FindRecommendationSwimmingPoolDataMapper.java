package com.project.swimcb.main.adapter.out;

import static com.project.swimcb.db.entity.QFavoriteEntity.favoriteEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolEntity.swimmingPoolEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolImageEntity.swimmingPoolImageEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolRatingEntity.swimmingPoolRatingEntity;
import static com.project.swimcb.db.entity.QSwimmingPoolReviewEntity.swimmingPoolReviewEntity;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static com.querydsl.core.types.Projections.constructor;

import com.project.swimcb.main.application.port.in.FindRecommendationSwimmingPoolDsGateway;
import com.project.swimcb.main.domain.FindRecommendationSwimmingPoolCondition;
import com.project.swimcb.main.domain.RecommendationSwimmingPool;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindRecommendationSwimmingPoolDataMapper implements FindRecommendationSwimmingPoolDsGateway {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<RecommendationSwimmingPool> findRecommendationSwimmingPools(
      @NonNull FindRecommendationSwimmingPoolCondition condition) {

    val distanceBetweenMemberAndSwimmingPool = distanceBetweenMemberAndSwimmingPool(
        condition.memberLatitude(), condition.memberLongitude());

    return queryFactory.select(constructor(QueryRecommendationSwimmingPool.class,
            swimmingPoolEntity.id,
            swimmingPoolImageEntity.path.min(),
            favoriteEntity.id.min(),
            distanceBetweenMemberAndSwimmingPool,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address,
            swimmingPoolRatingEntity.rating.avg(),
            swimmingPoolReviewEntity.id.sum()
        ))
        .from(swimmingPoolEntity)

        .join(swimmingPoolImageEntity)
        .on(swimmingPoolImageEntity.swimmingPool.eq(swimmingPoolEntity))

        .leftJoin(favoriteEntity)
        .on(favoriteJoinIfMemberIdExist(condition.memberId()))

        .leftJoin(swimmingPoolRatingEntity)
        .on(swimmingPoolRatingEntity.swimmingPool.eq(swimmingPoolEntity))

        .leftJoin(swimmingPoolReviewEntity)
        .on(swimmingPoolReviewEntity.swimmingPool.eq(swimmingPoolEntity))

        .where(
            swimmingPoolEntity.name.isNotNull(),
            swimmingPoolEntity.address.isNotNull(),
            swimmingPoolEntity.latitude.isNotNull(),
            swimmingPoolEntity.longitude.isNotNull()
        )
        .groupBy(
            swimmingPoolEntity.id,
            swimmingPoolEntity.longitude,
            swimmingPoolEntity.latitude,
            swimmingPoolEntity.name,
            swimmingPoolEntity.address
        )
        .orderBy(
            distanceBetweenMemberAndSwimmingPool.asc()
        )
        .limit(6)
        .fetch()
        .stream()
        .map(i -> RecommendationSwimmingPool.builder()
            .swimmingPoolId(i.swimmingPoolId())
            .imageUrl(i.imageUrl())
            .favoriteId(i.favoriteId())
            .distance((int) i.distance())
            .name(i.name())
            .address(i.address())
            .rating(i.rating())
            .reviewCount((int) i.reviewCount())
            .build()
        )
        .toList();

  }

  private NumberExpression<Double> distanceBetweenMemberAndSwimmingPool(double memberLatitude,
      double memberLongitude) {
    return Expressions.numberTemplate(Double.class,
        "(6371000 * acos(cos(radians({0})) * cos(radians({1})) * " +
            "cos(radians({2}) - radians({3})) + sin(radians({0})) * sin(radians({1}))))",
        memberLatitude, swimmingPoolEntity.latitude, swimmingPoolEntity.longitude, memberLongitude);
  }

  private BooleanExpression favoriteJoinIfMemberIdExist(Long memberId) {
    if (memberId == null) {
      return Expressions.FALSE;
    }
    return favoriteEntity.member.id.eq(memberId)
        .and(favoriteEntity.targetId.eq(swimmingPoolEntity.id))
        .and(favoriteEntity.targetType.eq(SWIMMING_POOL));
  }

  protected record QueryRecommendationSwimmingPool(
      long swimmingPoolId,
      @NonNull String imageUrl,
      Long favoriteId,
      double distance,
      @NonNull String name,
      @NonNull String address,
      double rating,
      long reviewCount
  ) {

  }

}
