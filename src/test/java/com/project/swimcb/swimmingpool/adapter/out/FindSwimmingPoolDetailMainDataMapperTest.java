package com.project.swimcb.swimmingpool.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.adapter.out.FindSwimmingPoolDetailMainDataMapper.SwimmingPool;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindSwimmingPoolDetailMainDataMapperTest {

  @InjectMocks
  private FindSwimmingPoolDetailMainDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  private JPAQuery<SwimmingPool> resultQuery;

  @SuppressWarnings("unchecked")
  @BeforeEach
  void setUp() {
    resultQuery = mock(JPAQuery.class);

    lenient().when(queryFactory.select(any(Expression.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.from(any(EntityPath.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.join(any(EntityPath.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.on(any(Predicate.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.on(any(Predicate[].class))).thenReturn(resultQuery);
    lenient().when(resultQuery.leftJoin(any(EntityPath.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.where(any(Predicate.class))).thenReturn(resultQuery);
    lenient().when(resultQuery.groupBy(any(Expression[].class))).thenReturn(resultQuery);
  }

  @Nested
  @DisplayName("수영장 메인 정보 조회시")
  class FindSwimmingPoolDetailMainTest {

    private final long swimmingPoolId = 1L;

    @Nested
    @DisplayName("수영장이 존재하는 경우")
    class WhenSwimmingPoolExists {

      @Test
      @DisplayName("수영장 상세 정보를 반환한다.")
      void shouldReturnSwimmingPoolDetailMain() {
        // given
        val memberId = 2L;
        val result = TestSwimmingPoolFactory.create();

        when(resultQuery.fetchFirst()).thenReturn(result);
        // when
        val poolMainInfo = mapper.findSwimmingPoolDetailMain(swimmingPoolId, memberId);
        // then
        assertThat(poolMainInfo.imagePaths()).hasSize(result.imagePaths().size());
        assertThat(poolMainInfo.name()).isEqualTo(result.name());
        assertThat(poolMainInfo.isFavorite()).isEqualTo(result.isFavorite());
        assertThat(poolMainInfo.rating()).isEqualTo(result.rating());
        assertThat(poolMainInfo.reviewCount()).isEqualTo(result.reviewCount());
        assertThat(poolMainInfo.address()).isEqualTo(result.address());
        assertThat(poolMainInfo.phone()).isEqualTo(result.phone());
      }
    }

    @Nested
    @DisplayName("조회되는 수영장이 없으면")
    class WhenSwimmingPooolDoesNotExist {

      @Test
      @DisplayName("NoSuchElementException 예외를 발생시킨다.")
      void shouldThrowNoSuchElementException() {
        // given
        when(resultQuery.fetchFirst()).thenReturn(null);
        // when
        // then
        assertThatThrownBy(() -> mapper.findSwimmingPoolDetailMain(swimmingPoolId, null))
            .isInstanceOf(NoSuchElementException.class);
      }
    }

    @Nested
    @DisplayName("favoriteJoinIfMemberIdExist 메서드 테스트")
    class FavoriteJoinIfMemberIdExistTest {

      @Nested
      @DisplayName("회원 ID가 null이면")
      class WhenMemberIdIsNull {

        @Test
        @DisplayName("false를 반환한다.")
        void shouldReturnFalseWhenMemberIdIsNull() {
          // given
          // when
          val result = mapper.favoriteJoinIfMemberIdExist(null);
          // then
          assertThat(result).isEqualTo(Expressions.FALSE);
        }
      }


      @Nested
      @DisplayName("회원 ID가 null이 아니면")
      class WhenMemberIdIsNotNull {

        @Test
        @DisplayName("조인 조건을 반환한다.")
        void shouldReturnFavoriteJoinConditionWhenMemberIdIsNotNull() {
          // given
          val memberId = 1L;
          // when
          val result = mapper.favoriteJoinIfMemberIdExist(memberId);
          // then
          assertThat(result)
              .isNotNull()
              .isNotEqualTo(Expressions.FALSE);
        }
      }
    }
  }

  private static class TestSwimmingPoolFactory {

    private static SwimmingPool create() {
      return new SwimmingPool(List.of("DUMMY_PATH1", "DUMMY_PATH2"), "DUMMY_NAME", true,
          4.5, 31, "DUMMY_ADDRESS", "DUMMY_PHONE");
    }
  }
}