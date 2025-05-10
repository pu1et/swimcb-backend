package com.project.swimcb.mypage.reservation.adapter.in;

import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.*;
import static java.time.DayOfWeek.MONDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.project.swimcb.bo.swimmingpool.application.out.ImageUrlPort;
import com.project.swimcb.mypage.reservation.adapter.out.ClassDayOfWeek;
import com.project.swimcb.mypage.reservation.domain.Reservation;
import com.project.swimcb.mypage.reservation.domain.Reservation.ReservationDetail;
import com.project.swimcb.mypage.reservation.domain.Reservation.Review;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingClass;
import com.project.swimcb.mypage.reservation.domain.Reservation.SwimmingPool;
import com.project.swimcb.mypage.reservation.domain.Reservation.Ticket;
import com.project.swimcb.swimmingpool.domain.enums.ReservationStatus;
import com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName;
import com.project.swimcb.swimmingpool.domain.enums.TicketType;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
class FindReservationsResponseMapperTest {

  @InjectMocks
  private FindReservationsResponseMapper mapper;

  @Mock
  private ImageUrlPort imageUrlPort;

  private String imagePath;
  private String imageUrl;
  private Page<Reservation> reservations;

  @BeforeEach
  void setUp() {
    imagePath = "DUMMY_IMAGE_PATH";
    imageUrl = "DUMMY_IMAGE_URL";

    lenient().when(imageUrlPort.getImageUrl(anyString())).thenReturn(imageUrl);

    val reservation = TestReservationFactory.create(imagePath);

    // Page 객체 생성
    val pageRequest = PageRequest.of(0, 10);
    reservations = new PageImpl<>(List.of(reservation), pageRequest, 1);
  }

  @Test
  @DisplayName("Reservation을 FindReservationsResponse로 변환 성공")
  void shouldConvertReservationToFindReservationsResponse() {
    // when
    val response = mapper.toResponse(reservations);

    // then
    assertThat(response).isNotNull();
    assertThat(response.reservations().getTotalElements()).isEqualTo(1);

    val responseItem = response.reservations().getContent().get(0);

    // SwimmingPool 검증
    assertThat(responseItem.swimmingPool().id()).isEqualTo(1L);
    assertThat(responseItem.swimmingPool().name()).isEqualTo("DUMMY_POOL_NAME");
    assertThat(responseItem.swimmingPool().imageUrl()).isEqualTo(imageUrl);

    // SwimmingClass 검증
    assertThat(responseItem.swimmingClass().id()).isEqualTo(2L);
    assertThat(responseItem.swimmingClass().month()).isEqualTo(7);
    assertThat(responseItem.swimmingClass().type()).isEqualTo(GROUP.getDescription());
    assertThat(responseItem.swimmingClass().subType()).isEqualTo("DUMMY_CLASS_SUB_TYPE");
    assertThat(responseItem.swimmingClass().days()).containsExactly("월", "수", "금");
    assertThat(responseItem.swimmingClass().startTime()).isEqualTo(LocalTime.of(18, 0));
    assertThat(responseItem.swimmingClass().endTime()).isEqualTo(LocalTime.of(19, 0));

    // Ticket 검증
    assertThat(responseItem.ticket().id()).isEqualTo(3L);
    assertThat(responseItem.ticket().name()).isEqualTo("DUMMY_TICKET_NAME");
    assertThat(responseItem.ticket().price()).isEqualTo(10000);

    // ReservationInfo 검증
    assertThat(responseItem.reservationInfo().id()).isEqualTo(4L);
    assertThat(responseItem.reservationInfo().ticketType()).isEqualTo(TicketType.SWIMMING_CLASS);
    assertThat(responseItem.reservationInfo().status()).isEqualTo(
        ReservationStatus.PAYMENT_PENDING.getDescription());
    assertThat(responseItem.reservationInfo().reservedAt()).isEqualTo(
        LocalDateTime.of(2025, 1, 1, 1, 1));
    assertThat(responseItem.reservationInfo().waitingNo()).isNull();

    // Review 검증
    assertThat(responseItem.review().id()).isEqualTo(5L);
  }

  @Test
  @DisplayName("빈 페이지가 주어진 경우 빈 응답 반환")
  void shouldReturnEmptyResponseWhenEmptyPageGiven() {
    // given
    final Page<Reservation> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

    // when
    val response = mapper.toResponse(emptyPage);

    // then
    assertThat(response).isNotNull();
    assertThat(response.reservations().getTotalElements()).isZero();
    assertThat(response.reservations().getContent()).isEmpty();
  }

  private static class TestReservationFactory {

    private static Reservation create(String imagePath) {
      return Reservation.builder()
          .swimmingPool(
              SwimmingPool.builder()
                  .id(1L)
                  .name("DUMMY_POOL_NAME")
                  .imagePath(imagePath)
                  .build()
          )
          .swimmingClass(
              SwimmingClass.builder()
                  .id(2L)
                  .month(7)
                  .type(GROUP)
                  .subType("DUMMY_CLASS_SUB_TYPE")
                  .daysOfWeek(ClassDayOfWeek.of(0b1010100)) // 월, 수, 금
                  .startTime(LocalTime.of(18, 0))
                  .endTime(LocalTime.of(19, 0))
                  .isCanceled(false)
                  .build()
          )
          .ticket(
              Ticket.builder()
                  .id(3L)
                  .name("DUMMY_TICKET_NAME")
                  .price(10000)
                  .build()
          )
          .reservationDetail(
              ReservationDetail.builder()
                  .id(4L)
                  .ticketType(TicketType.SWIMMING_CLASS)
                  .status(ReservationStatus.PAYMENT_PENDING)
                  .reservedAt(LocalDateTime.of(2025, 1, 1, 1, 1))
                  .waitingNo(null)
                  .build()
          )
          .review(
              Review.builder()
                  .id(5L)
                  .build()
          )
          .build();
    }
  }
}
