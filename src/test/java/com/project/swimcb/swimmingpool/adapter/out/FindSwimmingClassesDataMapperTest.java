package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BASIC;
import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BEGINNER;
import static com.project.swimcb.swimmingpool.domain.enums.Sort.DISTANCE_ASC;
import static com.project.swimcb.swimmingpool.domain.enums.Sort.PRICE_ASC;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.AQUA_AEROBICS;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.KIDS_SWIMMING;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.adapter.out.FindSwimmingClassesDataMapper.SwimmingPoolWithClass;
import com.project.swimcb.swimmingpool.domain.FindSwimmingClassesCondition;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindSwimmingClassesDataMapperTest {

  @InjectMocks
  private FindSwimmingClassesDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  private JPAQuery<SwimmingPoolWithClass> resultQuery;
  private JPAQuery<Long> countQuery;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    resultQuery = mock(JPAQuery.class);
    countQuery = mock(JPAQuery.class);

    lenient().when(queryFactory.select(any(Expression.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.from(any(EntityPath.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.join(any(EntityPath.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.on(any(Predicate.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.on(any(Predicate[].class))).thenReturn(resultQuery);
    lenient().when(resultQuery.leftJoin(any(EntityPath.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.where(any(Predicate[].class))).thenReturn(resultQuery);
    lenient().when(resultQuery.groupBy(any(Expression[].class))).thenReturn(resultQuery);
    lenient().when(resultQuery.orderBy(any(OrderSpecifier.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.offset(anyLong())).thenReturn(resultQuery);
    lenient().when(resultQuery.limit(anyLong())).thenReturn(resultQuery);

    lenient().when(queryFactory.select(any(NumberExpression.class))).thenReturn(countQuery);
    lenient().when(countQuery.from(any(EntityPath.class))).thenReturn(countQuery);
    lenient().when(countQuery.join(any(EntityPath.class))).thenReturn(countQuery);
    lenient().when(countQuery.on(any(Predicate.class))).thenReturn(countQuery);
    lenient().when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
    lenient().when(countQuery.where(any(BooleanExpression.class))).thenReturn(countQuery);
    lenient().when(countQuery.where(any(BooleanExpression[].class))).thenReturn(countQuery);
    lenient().when(countQuery.where(any(BooleanBuilder.class))).thenReturn(countQuery);
  }

  @Nested
  @DisplayName("findSwimmingClasses 메서드는")
  class FindSwimmingClassesTest {

    @Test
    @DisplayName("주어진 조건으로 수영 클래스를 조회하면 정상적인 결과를 반환한다.")
    void shouldReturnResponseWhenFindSwimmingClasses() {
      // given
      val condition = TestFindSwimmingClassesConditionFactory.create();

      val result = Arrays.asList(
          new SwimmingPoolWithClass(1L, "/path1", true, 2.5, "수영장1", "주소1", 4.5, 10L),
          new SwimmingPoolWithClass(2L, "/path2", false, 3.5, "수영장2", "주소2", 3.0, 1L)
      );

      when(resultQuery.fetch()).thenReturn(result);
      when(countQuery.fetchOne()).thenReturn(2L);
      // when
      val response = mapper.findSwimmingClasses(condition);
      // then
      assertThat(response).isNotNull();
      assertThat(response.classes()).hasSize(2);
      assertThat(response.classes().getTotalElements()).isEqualTo(2);
      assertThat(response.classes().getContent().getFirst().swimmingPoolId()).isEqualTo(1L);
      assertThat(response.classes().getContent().getFirst().imageUrl()).isEqualTo("/path1");
      assertThat(response.classes().getContent().getFirst().isFavorite()).isTrue();
      assertThat(response.classes().getContent().getFirst().distance()).isEqualTo(2);
      assertThat(response.classes().getContent().getFirst().rating()).isEqualTo(4.5);
      assertThat(response.classes().getContent().getFirst().reviewCount()).isEqualTo(10);

      assertThat(response.classes().getContent().get(1).swimmingPoolId()).isEqualTo(2L);
      assertThat(response.classes().getContent().get(1).imageUrl()).isEqualTo("/path2");
      assertThat(response.classes().getContent().get(1).isFavorite()).isFalse();
      assertThat(response.classes().getContent().get(1).distance()).isEqualTo(3);
      assertThat(response.classes().getContent().get(1).rating()).isEqualTo(3.0);
      assertThat(response.classes().getContent().get(1).reviewCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("조건에 맞는 수영 클래스가 없는 경우 빈 결과를 반환한다.")
    void shouldReturnEmptyResponseWhenNoSwimmingClasses() {
      // given
      val condition = TestFindSwimmingClassesConditionFactory.create();

      when(resultQuery.fetch()).thenReturn(List.of());
      when(countQuery.fetchOne()).thenReturn(0L);
      // when
      val response = mapper.findSwimmingClasses(condition);
      // then
      assertThat(response).isNotNull();
      assertThat(response.classes()).isEmpty();
      assertThat(response.classes().getTotalElements()).isZero();

      verify(resultQuery, times(1)).fetch();
      verify(countQuery, times(1)).fetchOne();
    }

    @Test
    @DisplayName("카운트 쿼리가 null을 반환하는 경우 0으로 처리한다.")
    void shouldReturnZeroWhenCountQueryIsNull() {
      // given
      val condition = TestFindSwimmingClassesConditionFactory.create();

      when(resultQuery.fetch()).thenReturn(List.of());
      when(countQuery.fetchOne()).thenReturn(null);
      // when
      val response = mapper.findSwimmingClasses(condition);
      // then
      assertThat(response).isNotNull();
      assertThat(response.classes()).isEmpty();
      assertThat(response.classes().getTotalElements()).isZero();

      verify(resultQuery, times(1)).fetch();
      verify(countQuery, times(1)).fetchOne();
    }
  }

  @Nested
  @DisplayName("distanceBetweenMemberAndSwimmingPool 메서드는")
  class DistanceBetweenMemberAndSwimmingPoolTest {

    @Test
    @DisplayName("회원 위치와 수영장 간의 거리 계산 표현식이 올바르게 생성된다.")
    void shouldReturnDistanceBetweenMemberAndSwimmingPoolExpression() {
      // given
      val memberLatitude = 12.345;
      val memberLongitude = 23.456;
      // when
      val expression = mapper.distanceBetweenMemberAndSwimmingPool(memberLatitude, memberLongitude);
      // then
      val expressionStr = expression.toString();
      assertThat(expressionStr)
          .isNotNull()
          .contains("6371000 * acos")
          .contains("radians(12.345)")
          .contains("radians(23.456)");
    }
  }

  @Nested
  @DisplayName("favoriteJoinIfMemberIdExist 메서드는")
  class FavoriteJoinIfMemberIdExistTest {

    @Test
    @DisplayName("memberId 가 null이면 FALSE를 반환한다.")
    void shouldReturnFalseWhenMemberIdIsNull() {
      // given
      // when
      val result = mapper.favoriteJoinIfMemberIdExist(null);
      // then
      assertThat(result).isEqualTo(Expressions.FALSE);
    }

    @Test
    @DisplayName("memberId가 null이 아니면 조인 조건을 생성한다.")
    void shouldReturnFavoriteJoinConditionWhenMemberIdIsNotNull() {
      // given
      val memberId = 1L;
      // when
      val result = mapper.favoriteJoinIfMemberIdExist(memberId);
      // then
      val resultString = result.toString();
      assertThat(resultString)
          .contains("favoriteEntity.member.id = 1")
          .contains("favoriteEntity.targetId = swimmingPoolEntity.id")
          .contains("favoriteEntity.targetType = SWIMMING_POOL");
    }
  }

  @Nested
  @DisplayName("swimmingPoolNameAndAddressContains 메서드는")
  class SwimmingPoolNameAndAddressContainsTest {

    @Test
    @DisplayName("키워드가 null이면 null을 반환한다.")
    void shouldReturnNullWhenKeywordIsNull() {
      // given
      // when
      val result = mapper.swimmingPoolNameAndAddressContains(null);
      // then
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("키워드가 있으면 수영장 이름 또는 주소에 대한 조건을 반환한다.")
    void shouldReturnBooleanBuilderWhenKeywordIsNotNull() {
      // given
      val keyword = "MOCK_KEYWORD";
      // when
      val result = mapper.swimmingPoolNameAndAddressContains(keyword);
      // then
      val resultString = result.toString();
      assertThat(resultString)
          .contains("swimmingPoolEntity.name", "MOCK_KEYWORD")
          .contains("||")
          .contains("swimmingPoolEntity.address", "MOCK_KEYWORD");
    }
  }

  @Nested
  @DisplayName("classTimeBetweenStartTimes 메서드는")
  class ClassTimeBetweenStartTimesTest {

    @Test
    @DisplayName("startTimes 리스트가 비어있는 경우 null을 반환한다.")
    void shouldReturnNullWhenStartTimesIsEmpty() {
      // given
      // when
      val builder = mapper.classTimeBetweenStartTimes(List.of());
      // then
      assertThat(builder).isNull();
    }

    @Test
    @DisplayName("startTimes에 시간이 하나만 있는 경우 해당 시간에 대한 조건이 포함된 BooleanBuilder를 반환한다.")
    void shouldReturnBooleanBuilderWhenStartTimesHasOneTime() {
      // given
      val startTimes = List.of(LocalTime.of(6, 0));
      // when
      val builder = mapper.classTimeBetweenStartTimes(startTimes);
      // then
      assertThat(builder.toString())
          .contains("startTime >= 06:00", "&&", "startTime < 07:00");
    }

    @Test
    @DisplayName("startTimes에 시간이 여러 개 있는 경우 각 시간에 대한 조건이 OR로 연결된 BooleanBuilder를 반환한다.")
    void shouldReturnBooleanBuilderWhenStartTimesHasMultipleTimes() {
      // given
      val startTimes = List.of(LocalTime.of(6, 0), LocalTime.of(17, 0));
      // when
      val builder = mapper.classTimeBetweenStartTimes(startTimes);
      // then
      assertThat(builder.toString())
          .contains("startTime >= 06:00", "&&", "startTime < 07:00")
          .contains("||")
          .contains("startTime >= 17:00", "&&", "startTime < 18:00");
    }
  }

  @Nested
  @DisplayName("sort 메서드는")
  class SortTest {

    @Test
    @DisplayName("DISTANCE_ASC이 주어지면 거리 기준 정렬자를 반환한다.")
    void shouldReturnDistanceOrderSpecifierWhenCreateDistanceOrderSpecifier() {
      // given
      final NumberExpression<Double> distanceExpression = mock(NumberExpression.class);
      final OrderSpecifier<Double> orderSpecifier = mock(OrderSpecifier.class);

      when(distanceExpression.asc()).thenReturn(orderSpecifier);
      // when
      val result = mapper.sort(DISTANCE_ASC, distanceExpression);
      // then
      assertThat(result).isEqualTo(orderSpecifier);
      verify(distanceExpression, only()).asc();
    }

    @Test
    @DisplayName("PRICE_ASC이 주어지면 가격 기준 정렬자를 반환한다.")
    void shouldReturnPriceOrderSpecifierWhenCreatePriceOrderSpecifier() {
      // given
      final NumberExpression<Double> distanceExpression = mock(NumberExpression.class);
      // when
      val result = mapper.sort(PRICE_ASC, distanceExpression);
      // then
      val resultString = result.toString();

      assertThat(resultString).contains("price ASC");
    }
  }

  @Nested
  @DisplayName("classTypeAndSubTypeIn 메서드는")
  class ClassTypeAndSubTypeTest {

    @Test
    @DisplayName("강습형태와 강습구분이 모두 empty면 null을 반환한다.")
    void shouldReturnNullWhenCreateEmptyClassTypeAndSubTypeIn() {
      // given
      // when
      val result = mapper.classTypeAndSubTypeIn(List.of(), List.of());
      // then
      assertThat(result).isNull();
    }

    @Test
    @DisplayName("강습형태와 강습구분이 모두 주어지면 올바른 필터 조건을 생성한다.")
    void shouldReturnBooleanBuilderWhenCreateClassTypeAndSubTypeIn() {
      // given
      val classTypes = List.of(GROUP, KIDS_SWIMMING, AQUA_AEROBICS);
      val classSubTypes = List.of(BASIC, BEGINNER);

      // when
      val result = mapper.classTypeAndSubTypeIn(classTypes, classSubTypes);
      // then
      assertThat(result).isNotNull();

      val resultString = result.toString();
      assertThat(resultString)
          .contains("GROUP && ", "in [BASIC, BEGINNER]")
          .contains("in [KIDS_SWIMMING, AQUA_AEROBICS]");
    }

    @Test
    @DisplayName("강습형태만 없으면 false를 반환한다.")
    void shouldReturnEmptyBooleanBuilderWhenCreateEmptyClassType() {
      // given
      val classSubTypes = List.of(BASIC, BEGINNER);
      // when
      val result = mapper.classTypeAndSubTypeIn(List.of(), classSubTypes);
      // then
      assertThat(result).isNotNull();
      assertThat(result.toString()).contains("false");
    }

    @Test
    @DisplayName("강습형태만 주어지고 강습구분이 없으면 강습형태 필터 조건만 생성한다.")
    void shouldReturnBooleanBuilderWhenCreateClassTypeIn() {
      // given
      val classTypes = List.of(GROUP, KIDS_SWIMMING, AQUA_AEROBICS);

      // when
      val result = mapper.classTypeAndSubTypeIn(classTypes, List.of());
      // then
      assertThat(result).isNotNull();

      val resultString = result.toString();
      assertThat(resultString)
          .contains("in [KIDS_SWIMMING, AQUA_AEROBICS]")
          .doesNotContain("and");
    }
  }

  @Nested
  @DisplayName("swimmingClassDaysOfWeek 메서드는")
  class SwimmingClassDaysOfWeekTest {

    @Test
    @DisplayName("요일 리스트가 비어있으면 null을 반환한다.")
    void shouldReturnNullWhenDaysIsEmpty() {
      // given
      // when
      val expression = mapper.swimmingClassDaysOfWeek(List.of());
      // then
      assertThat(expression).isNull();
    }

    @Test
    @DisplayName("요일 리스트가 있으면 daysOfWeek와 bitVector의 bitand 연산 표현식을 생성한다.")
    void shouldReturnDaysOfWeekExpressionWhenCreateDaysOfWeekExpression() {
      // given
      val days = List.of(MONDAY, WEDNESDAY, FRIDAY);
      // when
      val expression = mapper.swimmingClassDaysOfWeek(days);
      // then
      // 월(64), 수(16), 금(4)의 합인 84와 비교해야 한다.
      assertThat(expression.toString())
          .contains("bitand(swimmingClassEntity.daysOfWeek, 84) > 0");
    }
  }


  @Nested
  @DisplayName("bitVector 메서드는 ")
  class DaysToBitVectorTest {

    @Test
    @DisplayName("요일이 주어지지 않으면 0을 반환한다.")
    void shouldReturnZeroWhenDaysIsEmpty() {
      // given
      // when
      val bitVector = mapper.daysToBitVector(List.of());
      // then
      assertThat(bitVector).isZero();
    }

    @Test
    @DisplayName("요일이 주어지면 올바른 비트 벡터로 변환한다.")
    void shouldReturnDaysOfWeekExpressionWhenCreateDaysOfWeekExpression() {
      // given
      val days = List.of(MONDAY, WEDNESDAY, FRIDAY);
      // when
      val bitVector = mapper.daysToBitVector(days);
      // then
      // 월(64), 수(16), 금(4)의 합인 84가 나와야 한다.
      assertThat(bitVector).isEqualTo(84);
    }
  }

  private static class TestFindSwimmingClassesConditionFactory {

    private static FindSwimmingClassesCondition create() {
      return FindSwimmingClassesCondition.builder()
          .memberId(1L)
          .startDate(LocalDate.of(2025, 3, 1))
          .endDate(LocalDate.of(2025, 4, 1))
          .startTimes(List.of(LocalTime.of(6, 0), LocalTime.of(17, 0)))
          .days(List.of(MONDAY, TUESDAY, WEDNESDAY))
          .classTypes(List.of(GROUP, KIDS_SWIMMING))
          .classSubTypes(List.of(BASIC, BEGINNER))
          .memberLatitude(12.345)
          .memberLongitude(23.456)
          .pageable(PageRequest.of(0, 10))
          .sort(DISTANCE_ASC)
          .build();
    }
  }
}
