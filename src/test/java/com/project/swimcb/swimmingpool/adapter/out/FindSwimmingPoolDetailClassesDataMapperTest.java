package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BASIC;
import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BEGINNER;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesCondition;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse.SwimmingClassTicket;
import com.project.swimcb.swimmingpool.adapter.out.FindSwimmingPoolDetailClassesDataMapper.QuerySwimmingPoolDetailClass;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalTime;
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
class FindSwimmingPoolDetailClassesDataMapperTest {

  @InjectMocks
  private FindSwimmingPoolDetailClassesDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  private JPAQuery<QuerySwimmingPoolDetailClass> resultQuery;
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
    lenient().when(resultQuery.leftJoin(any(EntityPath.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.where(any(Predicate[].class))).thenReturn(resultQuery);
    lenient().when(resultQuery.groupBy(any(Expression[].class))).thenReturn(resultQuery);
    lenient().when(resultQuery.offset(anyLong())).thenReturn(resultQuery);
    lenient().when(resultQuery.limit(anyLong())).thenReturn(resultQuery);

    lenient().when(queryFactory.select(any(NumberExpression.class))).thenReturn(countQuery);
    lenient().when(countQuery.from(any(EntityPath.class))).thenReturn(countQuery);
    lenient().when(countQuery.join(any(EntityPath.class))).thenReturn(countQuery);
    lenient().when(countQuery.on(any(Predicate.class))).thenReturn(countQuery);
    lenient().when(countQuery.where(any(Predicate[].class))).thenReturn(countQuery);
  }

  @Nested
  @DisplayName("findSwimmingPoolDetailClasses 메서드는")
  class FindSwimmingPoolDetailClasses {

    @Test
    @DisplayName("주어진 조건으로 수영 클래스를 조회하면 정상적인 결과를 반환한다.")
    void shouldReturnSwimmingClassesWhenFindSwimmingClasses() {
      // given
      val condition = TestFindSwimmingPoolDetailClassesConditionFactory.create();

      // GROUP - DUMMY_SUB_TYPE1 - 티켓 2개
      // GROUP - DUMMY_SUB_TYPE2 - 티켓 1개
      // KIDS_SWIMMING - DUMMY_SUB_TYPE3 - 티켓 2개
      // KIDS_SWIMMING - DUMMY_SUB_TYPE4 - 티켓 1개
      val result = List.of(
          new QuerySwimmingPoolDetailClass(1L, GROUP, "DUMMY_SUB_TYPE1", 112, LocalTime.of(6, 0),
              LocalTime.of(7, 0), 9000, true, 10, 5, 1L, "DUMMY_TICKET1", 9000),
          new QuerySwimmingPoolDetailClass(1L, GROUP, "DUMMY_SUB_TYPE1", 112, LocalTime.of(6, 0),
              LocalTime.of(7, 0), 9000, true, 10, 5, 2L, "DUMMY_TICKET2", 10000),

          new QuerySwimmingPoolDetailClass(2L, GROUP, "DUMMY_SUB_TYPE2", 3, LocalTime.of(7, 0),
              LocalTime.of(8, 0), 20000, false, 20, 19, 3L, "DUMMY_TICKET3", 20000),

          new QuerySwimmingPoolDetailClass(3L, KIDS_SWIMMING, "DUMMY_SUB_TYPE3", 84,
              LocalTime.of(17, 0), LocalTime.of(18, 0), 30000, false, 25, 20, 4L, "DUMMY_TICKET4",
              30000),
          new QuerySwimmingPoolDetailClass(3L, KIDS_SWIMMING, "DUMMY_SUB_TYPE3", 84,
              LocalTime.of(17, 0), LocalTime.of(18, 0), 30000, false, 25, 20, 5L, "DUMMY_TICKET5",
              30000),

          new QuerySwimmingPoolDetailClass(4L, KIDS_SWIMMING, "DUMMY_SUB_TYPE4", 5,
              LocalTime.of(18, 0), LocalTime.of(19, 0), 40000, true, 44, 40, 6L, "DUMMY_TICKET6",
              40000)
      );

      when(resultQuery.fetch()).thenReturn(result);
      when(countQuery.fetchOne()).thenReturn(2L);
      // when
      val response = mapper.findSwimmingPoolDetailClasses(condition);
      // then
      assertThat(response).isNotNull();
      assertThat(response.classes()).hasSize(4);
      assertThat(response.classes().getTotalElements()).isEqualTo(4);

      val content = response.classes().getContent();
      val class1 = content.get(0);

      assertThat(class1).isNotNull();
      assertThat(class1.swimmingClassId()).isEqualTo(1L);
      assertThat(class1.type()).isEqualTo(GROUP.getDescription());
      assertThat(class1.subType()).isEqualTo("DUMMY_SUB_TYPE1");
      assertThat(class1.days()).containsExactly("월", "화", "수");
      assertThat(class1.startTime()).isEqualTo(LocalTime.of(6, 0));
      assertThat(class1.isReservable()).isTrue();
      assertThat(class1.tickets()).hasSize(2);
      assertThat(class1.tickets()).extracting(SwimmingClassTicket::swimmingClassTicketId)
          .containsExactly(1L, 2L);

      val class2 = content.get(1);
      assertThat(class2).isNotNull();
      assertThat(class2.swimmingClassId()).isEqualTo(2L);
      assertThat(class2.type()).isEqualTo(GROUP.getDescription());
      assertThat(class2.subType()).isEqualTo("DUMMY_SUB_TYPE2");
      assertThat(class2.days()).containsExactly("토", "일");
      assertThat(class2.isReservable()).isTrue();
      assertThat(class2.tickets()).hasSize(1);
      assertThat(class2.tickets()).extracting(SwimmingClassTicket::swimmingClassTicketId).containsExactly(3L);

      val class3 = content.get(2);
      assertThat(class3).isNotNull();
      assertThat(class3.swimmingClassId()).isEqualTo(3L);
      assertThat(class3.type()).isEqualTo(KIDS_SWIMMING.getDescription());
      assertThat(class3.subType()).isEqualTo("DUMMY_SUB_TYPE3");
      assertThat(class3.days()).containsExactly("월", "수", "금");
      assertThat(class3.isReservable()).isTrue();
      assertThat(class3.tickets()).hasSize(2);
      assertThat(class3.tickets()).extracting(SwimmingClassTicket::swimmingClassTicketId).containsExactly(4L, 5L);

      val class4 = content.get(3);
      assertThat(class4).isNotNull();
      assertThat(class4.swimmingClassId()).isEqualTo(4L);
      assertThat(class4.type()).isEqualTo(KIDS_SWIMMING.getDescription());
      assertThat(class4.subType()).isEqualTo("DUMMY_SUB_TYPE4");
      assertThat(class4.days()).containsExactly("금", "일");
      assertThat(class4.isReservable()).isTrue();
      assertThat(class4.tickets()).hasSize(1);
      assertThat(class4.tickets()).extracting(SwimmingClassTicket::swimmingClassTicketId).containsExactly(6L);

      verify(resultQuery, times(1)).fetch();
    }

    @Test
    @DisplayName("조건에 맞는 수영 클래스가 없는 경우 빈 결과를 반환한다.")
    void shouldReturnEmptyResultWhenNoSwimmingClasses() {
      // given
      val condition = TestFindSwimmingPoolDetailClassesConditionFactory.create();

      when(resultQuery.fetch()).thenReturn(List.of());
      when(countQuery.fetchOne()).thenReturn(0L);
      // when
      val response = mapper.findSwimmingPoolDetailClasses(condition);
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
      val condition = TestFindSwimmingPoolDetailClassesConditionFactory.create();

      when(resultQuery.fetch()).thenReturn(List.of());
      when(countQuery.fetchOne()).thenReturn(null);
      // when
      val response = mapper.findSwimmingPoolDetailClasses(condition);
      // then
      assertThat(response).isNotNull();
      assertThat(response.classes()).isEmpty();
      assertThat(response.classes().getTotalElements()).isZero();

      verify(resultQuery, times(1)).fetch();
      verify(countQuery, times(1)).fetchOne();
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
          .contains("favoriteEntity.targetId = swimmingClassEntity.id")
          .contains("favoriteEntity.targetType = SWIMMING_CLASS");
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
  @DisplayName("daysToBitVector 메서드는 ")
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
  
  @Nested
  @DisplayName("bitVectorToDays 메서드는")
  class BitVectorToDaysTest {

    @Test
    @DisplayName("비트 벡터가 주어지지 않으면 빈 리스트를 반환한다.")
    void shouldReturnEmptyListWhenBitVectorIsZero() {
      // given
      // when
      val days = mapper.bitVectorToDays(0);
      // then
      assertThat(days).isEmpty();
    }

    @Test
    @DisplayName("비트 벡터가 주어지면 올바른 요일 리스트로 변환한다.")
    void shouldReturnDaysOfWeekExpressionWhenCreateDaysOfWeekExpression() {
      // given
      val bitVector = 84;
      // when
      val days = mapper.bitVectorToDays(bitVector);
      // then
      assertThat(days).containsExactly("월", "수", "금");
    }
  }

  private static class TestFindSwimmingPoolDetailClassesConditionFactory {

    private static FindSwimmingPoolDetailClassesCondition create() {
      return FindSwimmingPoolDetailClassesCondition.builder()
          .swimmingPoolId(1L)
          .startDate(LocalDate.of(2025, 3, 1))
          .endDate(LocalDate.of(2025, 4, 1))
          .startTimes(List.of(LocalTime.of(6, 0), LocalTime.of(17, 0)))
          .days(List.of(MONDAY, TUESDAY, WEDNESDAY))
          .classTypes(List.of(GROUP, KIDS_SWIMMING))
          .classSubTypes(List.of(BASIC, BEGINNER))
          .pageable(PageRequest.of(0, 10))
          .memberId(2L)
          .build();
    }
  }
}
