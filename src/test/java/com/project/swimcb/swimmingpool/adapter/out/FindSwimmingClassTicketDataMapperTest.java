package com.project.swimcb.swimmingpool.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.project.swimcb.swimmingpool.adapter.out.FindSwimmingClassTicketDataMapper.QuerySwimmingClassTicketInfo;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalTime;
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
class FindSwimmingClassTicketDataMapperTest {

  @InjectMocks
  private FindSwimmingClassTicketDataMapper mapper;

  @Mock
  private JPAQueryFactory queryFactory;

  @Mock
  private JPAQuery<QuerySwimmingClassTicketInfo> query;

  private static final long TICKET_ID = 1L;

  @BeforeEach
  void setUp() {
    lenient().when(queryFactory.select(any(Expression.class))).thenReturn(query);
    lenient().when(query.from(any(EntityPath.class))).thenReturn(query);
    lenient().when(query.join(any(EntityPath.class))).thenReturn(query);
    lenient().when(query.on(any(Predicate.class))).thenReturn(query);
    lenient().when(query.where(any(Predicate.class))).thenReturn(query);
  }

  @Nested
  @DisplayName("수영장 티켓 정보 조회시")
  class FindSwimmingClassTicketTest {

    @Nested
    @DisplayName("수영장 티켓 정보가 존재하는 경우")
    class WhenSwimmingClassTicketInfoExists {

      @Test
      @DisplayName("수영장 티켓 정보를 반환한다.")
      void shouldReturnSwimmingClassTicketInfo() {
        // given
        val result = TestQuerySwimmingClassTicketInfoFactory.create();

        when(query.fetchFirst()).thenReturn(result);
        // when
        val ticketInfo = mapper.findSwimmingClassTicket(TICKET_ID);
        // then
        assertThat(ticketInfo).isNotNull();

        val classInfo = ticketInfo.swimmingClass();
        assertThat(classInfo.id()).isEqualTo(result.swimmingClassId());
        assertThat(classInfo.type()).isEqualTo(result.clssType().getDescription());
        assertThat(classInfo.subType()).isEqualTo(result.classSubType());
        assertThat(classInfo.days()).isEqualTo(List.of("월", "화", "수"));
        assertThat(classInfo.startTime()).isEqualTo(result.startTime());
        assertThat(classInfo.endTime()).isEqualTo(result.endTime());
      }
    }

    @Nested
    @DisplayName("수영장 티켓 정보가 존재하지 않는 경우")
    class WhenSwimmingClassTicketInfoNotExists {

      @Test
      @DisplayName("NoSuchElementException 예외를 발생시킨다.")
      void shouldThrowNoSuchElementException() {
        // given
        when(query.fetchFirst()).thenReturn(null);
        // when
        // then
        assertThatThrownBy(() -> mapper.findSwimmingClassTicket(TICKET_ID))
            .isInstanceOf(NoSuchElementException.class);
      }
    }
  }

  @Nested
  @DisplayName("bitVectorToDays 메서드는")
  class BitVectorToDaysTest {

    @Test
    @DisplayName("비트 벡터가 주어지면 올바른 요일 리스트로 변환한다.")
    void shouldConvertBitVectorToDays() {
      // given
      val bitVector = 84;
      // when
      val days = mapper.bitVectorToDays(bitVector);
      // then
      assertThat(days).containsExactly("월", "수", "금");
    }

    @Test
    @DisplayName("비트 벡터가 0이면 빈 리스트를 반환한다.")
    void shouldReturnEmptyListWhenBitVectorIsZero() {
      // given
      // when
      val days = mapper.bitVectorToDays(0);
      // then
      assertThat(days).isEmpty();
    }

    @Test
    @DisplayName("모든 요일이 선택된 비트 벡터를 반환한다.")
    void shouldReturnAllDaysWhenAllDaysSelected() {
      // given
      // when
      val days = mapper.bitVectorToDays(127);
      // then
      assertThat(days).hasSize(7);
    }
  }

  private static class TestQuerySwimmingClassTicketInfoFactory {

    private static QuerySwimmingClassTicketInfo create() {
      return new QuerySwimmingClassTicketInfo(
          1L,
          GROUP,
          "DUMMY_SUB_TYPE",
          112,
          LocalTime.of(10, 0),
          LocalTime.of(11, 0),
          "DUMMY_TICKET_NAME",
          10000
      );
    }
  }
}