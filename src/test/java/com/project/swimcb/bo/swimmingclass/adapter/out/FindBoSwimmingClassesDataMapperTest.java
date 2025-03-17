package com.project.swimcb.bo.swimmingclass.adapter.out;

import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.KIDS_SWIMMING;
import static java.time.DayOfWeek.FRIDAY;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.THURSDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingclass.adapter.in.FindBoSwimmingClassesResponse.Ticket;
import com.project.swimcb.bo.swimmingclass.adapter.out.FindBoSwimmingClassesDataMapper.BoSwimmingClass;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FindBoSwimmingClassesDataMapperTest {

  private FindBoSwimmingClassesDataMapper mapper;

  @Mock
  private EntityManager entityManager;

  @Mock
  private EntityManagerFactory entityManagerFactory;

  @Mock
  private Query query;

  @BeforeEach
  void setUp() {
    when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
    when(entityManager.createQuery(anyString())).thenReturn(query);

    mapper = spy(new FindBoSwimmingClassesDataMapper(entityManager));
  }

  @Test
  @DisplayName("조회된 클래스 데이터에 맞게 정상적으로 가공하여 반환한다.")
  void shouldMapSwimmingClassTypeAndClassTypeToResponse() {
    // given
    val swimmingPoolId = 1L;
    val month = 3;
    val result = TestBoSwimmingClassesFactory.create();

    when(mapper.findSwimmingClasses(anyLong(), anyInt())).thenReturn(result);
    // when
    val response = mapper.findBySwimmingPoolId(swimmingPoolId, month);
    // then
    assertThat(response.swimmingClasses()).hasSize(2);

    // 클래스 2개에 티켓 2개씩 매핑되는지 확인
    val swimmingClass1 = response.swimmingClasses().get(0);
    val swimmingClass2 = response.swimmingClasses().get(1);

    assertThat(swimmingClass1.swimmingClassId()).isEqualTo(1L);
    assertThat(swimmingClass1.type().typeId()).isEqualTo(2L);
    assertThat(swimmingClass1.type().typeName()).isEqualTo(GROUP.getDescription());
    assertThat(swimmingClass1.type().subTypeId()).isEqualTo(3L);
    assertThat(swimmingClass1.type().subTypeName()).isEqualTo("DUMMY_SUB_TYPE_NAME3");
    assertThat(swimmingClass1.days()).isEqualTo(List.of(MONDAY, WEDNESDAY, FRIDAY));
    assertThat(swimmingClass1.time().startTime()).isEqualTo(LocalTime.of(9, 0));
    assertThat(swimmingClass1.instructor().id()).isEqualTo(4L);
    assertThat(swimmingClass1.ticketPriceRange().minimumPrice()).isEqualTo(10000);
    assertThat(swimmingClass1.ticketPriceRange().maximumPrice()).isEqualTo(20000);
    assertThat(swimmingClass1.tickets()).hasSize(2);
    assertThat(swimmingClass1.tickets()).extracting(Ticket::id)
        .containsExactly(5L, 6L);
    assertThat(swimmingClass1.tickets()).extracting(Ticket::name)
        .containsExactly("DUMMY_TICKET_NAME5", "DUMMY_TICKET_NAME6");
    assertThat(swimmingClass1.tickets()).extracting(Ticket::price)
        .containsExactly(10000, 20000);
    assertThat(swimmingClass1.registrationCapacity().totalCapacity()).isEqualTo(10);

    assertThat(swimmingClass2.swimmingClassId()).isEqualTo(2L);
    assertThat(swimmingClass2.type().typeId()).isEqualTo(3L);
    assertThat(swimmingClass2.type().typeName()).isEqualTo(KIDS_SWIMMING.getDescription());
    assertThat(swimmingClass2.type().subTypeId()).isEqualTo(4L);
    assertThat(swimmingClass2.type().subTypeName()).isEqualTo("DUMMY_SUB_TYPE_NAME4");
    assertThat(swimmingClass2.days()).isEqualTo(List.of(TUESDAY, THURSDAY));
    assertThat(swimmingClass2.time().startTime()).isEqualTo(LocalTime.of(6, 0));
    assertThat(swimmingClass2.instructor().id()).isEqualTo(5L);
    assertThat(swimmingClass2.ticketPriceRange().minimumPrice()).isEqualTo(30000);
    assertThat(swimmingClass2.ticketPriceRange().maximumPrice()).isEqualTo(40000);
    assertThat(swimmingClass2.tickets()).hasSize(2);
    assertThat(swimmingClass2.tickets()).extracting(Ticket::id)
        .containsExactly(7L, 8L);
    assertThat(swimmingClass2.tickets()).extracting(Ticket::name)
        .containsExactly("DUMMY_TICKET_NAME7", "DUMMY_TICKET_NAME8");
    assertThat(swimmingClass2.tickets()).extracting(Ticket::price)
        .containsExactly(30000, 40000);
    assertThat(swimmingClass2.registrationCapacity().totalCapacity()).isEqualTo(20);
  }

  @Test
  @DisplayName("클래스가 없으면 빈 리스트를 반환한다.")
  void shouldReturnEmptyListWhenNoClass() {
    // given
    val swimmingPoolId = 1L;
    val month = 3;

    when(mapper.findSwimmingClasses(anyLong(), anyInt())).thenReturn(List.of());
    // when
    val response = mapper.findBySwimmingPoolId(swimmingPoolId, month);
    // then
    assertThat(response.swimmingClasses()).isEmpty();
  }

  private static class TestBoSwimmingClassesFactory {

    private static List<BoSwimmingClass> create() {
      // 클래스 2개, 각 클래스에 강습형태 1개, 강습구분 1개, 강사 1개, 티켓 2개
      return List.of(
          BoSwimmingClass.builder()
              .swimmingClassId(1L)
              .swimmingClassTypeId(2L)
              .classTypeName(GROUP)
              .classSubTypeId(3L)
              .classSubTypeName("DUMMY_SUB_TYPE_NAME3")
              .daysOfWeek(84)
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .instructorId(4L)
              .instructorName("DUMMY_INSTRUCTOR_NAME4")
              .ticketId(5L)
              .ticketName("DUMMY_TICKET_NAME5")
              .ticketPrice(10000)
              .totalCapacity(10)
              .reservationLimitCount(5)
              .isExposed(true)
              .build(),
          BoSwimmingClass.builder()
              .swimmingClassId(1L)
              .swimmingClassTypeId(2L)
              .classTypeName(GROUP)
              .classSubTypeId(3L)
              .classSubTypeName("DUMMY_SUB_TYPE_NAME3")
              .daysOfWeek(84)
              .startTime(LocalTime.of(9, 0))
              .endTime(LocalTime.of(10, 0))
              .instructorId(4L)
              .instructorName("DUMMY_INSTRUCTOR_NAME4")
              .ticketId(6L)
              .ticketName("DUMMY_TICKET_NAME6")
              .ticketPrice(20000)
              .totalCapacity(10)
              .reservationLimitCount(5)
              .isExposed(true)
              .build(),
          BoSwimmingClass.builder()
              .swimmingClassId(2L)
              .swimmingClassTypeId(3L)
              .classTypeName(KIDS_SWIMMING)
              .classSubTypeId(4L)
              .classSubTypeName("DUMMY_SUB_TYPE_NAME4")
              .daysOfWeek(40)
              .startTime(LocalTime.of(6, 0))
              .endTime(LocalTime.of(7, 0))
              .instructorId(5L)
              .instructorName("DUMMY_INSTRUCTOR_NAME5")
              .ticketId(7L)
              .ticketName("DUMMY_TICKET_NAME7")
              .ticketPrice(30000)
              .totalCapacity(20)
              .reservationLimitCount(10)
              .isExposed(false)
              .build(),
          BoSwimmingClass.builder()
              .swimmingClassId(2L)
              .swimmingClassTypeId(3L)
              .classTypeName(KIDS_SWIMMING)
              .classSubTypeId(4L)
              .classSubTypeName("DUMMY_SUB_TYPE_NAME4")
              .daysOfWeek(40)
              .startTime(LocalTime.of(6, 0))
              .endTime(LocalTime.of(7, 0))
              .instructorId(5L)
              .instructorName("DUMMY_INSTRUCTOR_NAME5")
              .ticketId(8L)
              .ticketName("DUMMY_TICKET_NAME8")
              .ticketPrice(40000)
              .totalCapacity(20)
              .reservationLimitCount(10)
              .isExposed(false)
              .build()
      );
    }
  }
}