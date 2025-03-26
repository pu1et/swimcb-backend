package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassControllerTest.MEMBER_ID;
import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BASIC;
import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BEGINNER;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.GROUP;
import static com.project.swimcb.swimmingpool.domain.enums.SwimmingClassTypeName.KIDS_SWIMMING;
import static java.time.DayOfWeek.MONDAY;
import static java.time.DayOfWeek.TUESDAY;
import static java.time.DayOfWeek.WEDNESDAY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import com.project.swimcb.common.WithMockTokenInfo;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse.SwimmingClass;
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingPoolDetailClassesResponse.SwimmingClassTicket;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingPoolDetailClassesUseCase;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = FindSwimmingPoolDetailClassController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindSwimmingPoolDetailClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindSwimmingPoolDetailClassesUseCase useCase;

  static final String MEMBER_ID = "1";

  private static final String PATH = "/api/swimming-pools/2/classes";
  private static final long SWIMMING_POOL_ID = 2L;

  @Test
  @DisplayName("수영 상세 - 클래스 조회 요청을 성공적으로 처리한다.")
  void shouldReturnResponseWhenSwimmingClassesExist() throws Exception {
    // given
    val startDate = LocalDate.of(2025, 3, 1);
    val endDate = LocalDate.of(2025, 4, 1);
    val startTimes = List.of(LocalTime.of(6, 0), LocalTime.of(17, 0));
    val days = List.of(MONDAY, TUESDAY, WEDNESDAY);
    val classTypes = List.of(GROUP, KIDS_SWIMMING);
    val classSubTypes = List.of(BASIC, BEGINNER);
    val page = 1;
    val size = 10;
    val response = TestFindSwimmingPoolDetailClassesResponseFactory.create();

    when(useCase.findSwimmingPoolDetailClasses(any())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("start-date", startDate.toString())
            .param("end-date", endDate.toString())
            .param("start-times", "06:00", "17:00")
            .param("days", "MONDAY", "TUESDAY", "WEDNESDAY")
            .param("class-types", "GROUP", "KIDS_SWIMMING")
            .param("class-sub-types", "BASIC", "BEGINNER")
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size)))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findSwimmingPoolDetailClasses(assertArg(i -> {
      assertThat(i.swimmingPoolId()).isEqualTo(SWIMMING_POOL_ID);
      assertThat(i.memberId()).isEqualTo(Long.parseLong(MEMBER_ID));
      assertThat(i.startDate()).isEqualTo(startDate);
      assertThat(i.endDate()).isEqualTo(endDate);
      assertThat(i.startTimes()).isEqualTo(startTimes);
      assertThat(i.days()).isEqualTo(days);
      assertThat(i.classTypes()).isEqualTo(classTypes);
      assertThat(i.classSubTypes()).isEqualTo(classSubTypes);
      assertThat(i.pageable().getPageNumber()).isEqualTo(page - 1);
      assertThat(i.pageable().getPageSize()).isEqualTo(size);
    }));
  }

  private static class TestFindSwimmingPoolDetailClassesResponseFactory {

    private static FindSwimmingPoolDetailClassesResponse create() {
      val classes = List.of(SwimmingClass.builder()
          .swimmingClassId(1L)
          .type(GROUP.getDescription())
          .subType(BEGINNER.getDescription())
          .days(List.of("월", "화", "수"))
          .startTime(LocalTime.of(6, 0))
          .endTime(LocalTime.of(7, 0))
          .minimumPrice(10000)
          .isFavorite(true)
          .isReservable(true)
          .tickets(List.of(
              SwimmingClassTicket.builder()
                  .swimmingClassTicketId(1L)
                  .name("DUMMY_TICKET_NAME1")
                  .price(10000)
                  .build(),
              SwimmingClassTicket.builder()
                  .swimmingClassTicketId(2L)
                  .name("DUMMY_TICKET_NAME2")
                  .price(20000)
                  .build()))
          .build());
      return new FindSwimmingPoolDetailClassesResponse(new PageImpl<>(classes));
    }
  }
}