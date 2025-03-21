package com.project.swimcb.swimmingpool.adapter.out;


import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPool.swimmingPool;
import static com.project.swimcb.bo.swimmingpool.domain.QSwimmingPoolImage.swimmingPoolImage;
import static com.project.swimcb.favorite.domain.QFavorite.favorite;
import static com.project.swimcb.favorite.domain.enums.FavoriteTargetType.SWIMMING_POOL;
import static com.project.swimcb.swimming_pool_rating.application.in.QSwimmingPoolRating.swimmingPoolRating;
import static com.project.swimcb.swimming_pool_review.domain.QSwimmingPoolReview.swimmingPoolReview;
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
            list(swimmingPoolImage.path),
            swimmingPool.name,
            favorite.id.count().gt(0),
            swimmingPoolRating.rating.avg(),
            swimmingPoolReview.id.sum(),
            swimmingPool.address,
            swimmingPool.phone
        ))
        .from(swimmingPool)
        .join(swimmingPoolImage).on(swimmingPoolImage.swimmingPool.eq(swimmingPool))
        .leftJoin(favorite).on(favoriteJoinIfMemberIdExist(memberId))
        .leftJoin(swimmingPoolRating).on(swimmingPoolRating.swimmingPool.eq(swimmingPool))
        .leftJoin(swimmingPoolReview).on(swimmingPoolReview.swimmingPool.eq(swimmingPool))
        .where(swimmingPool.id.eq(swimmingPoolId))
        .groupBy(
            swimmingPool.name,
            swimmingPool.address,
            swimmingPool.phone,
            swimmingPoolImage.path
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
        .build();
  }

  BooleanExpression favoriteJoinIfMemberIdExist(Long memberId) {
    if (memberId == null) {
      return Expressions.FALSE;
    }
    return favorite.member.id.eq(memberId)
        .and(favorite.targetId.eq(swimmingPool.id))
        .and(favorite.targetType.eq(SWIMMING_POOL));
  }

  public record SwimmingPool(
      @NonNull List<String> imagePaths,
      @NonNull String name,
      boolean isFavorite,
      double rating,
      long reviewCount,
      @NonNull String address,
      @NonNull String phone
  ) {

  }
}
