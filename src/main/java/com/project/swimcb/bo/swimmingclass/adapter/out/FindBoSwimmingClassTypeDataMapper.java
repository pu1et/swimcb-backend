package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.ClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassSubType.swimmingClassSubType;
import static com.project.swimcb.bo.swimmingclass.domain.QSwimmingClassType.swimmingClassType;
import static com.querydsl.core.types.Projections.constructor;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse;
import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassTypesResponse.ClassType;
import com.project.swimcb.bo.swimmingclass.application.out.FindBoSwimmingClassTypeDsGateway;
import com.querydsl.core.annotations.QueryProjection;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.Builder;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class FindBoSwimmingClassTypeDataMapper implements FindBoSwimmingClassTypeDsGateway {

  private final JPAQueryFactory queryFactory;

  public FindBoSwimmingClassTypeDataMapper(EntityManager entityManager) {
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  @Override
  public FindBoSwimmingClassTypesResponse findBoSwimmingClassTypes(long swimmingPoolId) {
    val result = findClassTypeWithSubTypes(swimmingPoolId);

    val classTypeIdAndNameMap = result.stream().collect(toMap(
        i -> i.swimmingClassTypeId,
        i -> i.swimmingClassTypeName,
        (existing, replacement) -> existing));

    val classTypeWithSubTypes = result
        .stream()
        .collect(groupingBy(i -> i.swimmingClassTypeId))
        .entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .map(i -> {
          val key = i.getKey();
          val value = i.getValue();

          return ClassType.builder()
              .classTypeId(key)
              .name(classTypeIdAndNameMap.get(key))
              .classSubTypes(
                  value.stream()
                      .filter(k -> k.swimmingClassSubTypeId != null)
                      .map(
                          k -> ClassSubType.builder()
                                .classSubTypeId(k.swimmingClassSubTypeId)
                                .name(k.swimmingClassSubTypeName)
                                .build()
                      )
                      .distinct()
                      .toList()
              )
              .build();
        }).toList();

    return new FindBoSwimmingClassTypesResponse(classTypeWithSubTypes);
  }

  List<SwimmingClassTypeWithSubType> findClassTypeWithSubTypes(long swimmingPoolId) {
    return queryFactory.select(
            constructor(SwimmingClassTypeWithSubType.class,
                swimmingClassType.id,
                swimmingClassType.name,
                swimmingClassSubType.id,
                swimmingClassSubType.name
            ))
        .from(swimmingClassType)
        .leftJoin(swimmingClassSubType)
        .on(swimmingClassSubType.swimmingClassType.eq(swimmingClassType))
        .where(
            swimmingClassType.swimmingPool.id.eq(swimmingPoolId)
        )
        .fetch();
  }

  @Builder
  public record SwimmingClassTypeWithSubType(
      long swimmingClassTypeId,
      String swimmingClassTypeName,
      Long swimmingClassSubTypeId,
      String swimmingClassSubTypeName
  ) {

    @QueryProjection
    public SwimmingClassTypeWithSubType {
    }
  }
}
