package com.project.swimcb.swimmingpool.adapter.in;

import static com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassControllerTest.MEMBER_ID;
import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BASIC;
import static com.project.swimcb.swimmingpool.domain.enums.GroupFixedClassSubTypeName.BEGINNER;
import static com.project.swimcb.swimmingpool.domain.enums.Sort.DISTANCE_ASC;
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
import com.project.swimcb.swimmingpool.adapter.in.FindSwimmingClassesResponse.SwimmingClass;
import com.project.swimcb.swimmingpool.application.in.FindSwimmingClassesUseCase;
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

@WebMvcTestWithoutSecurity(controllers = FindSwimmingClassController.class)
@WithMockTokenInfo(memberId = MEMBER_ID)
class FindSwimmingClassControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private FindSwimmingClassesUseCase useCase;

  static final String MEMBER_ID = "1";

  private static final String PATH = "/api/swimming-classes";

  @Test
  @DisplayName("수영 클래스 조회 요청을 성공적으로 처리한다.")
  void shouldReturnResponseWhenSwimmingClassesExist() throws Exception {
    // given
    val startDate = LocalDate.of(2025, 3, 1);
    val endDate = LocalDate.of(2025, 4, 1);
    val startTimes = List.of(LocalTime.of(6, 0), LocalTime.of(17, 0));
    val days = List.of(MONDAY, TUESDAY, WEDNESDAY);
    val classTypes = List.of(GROUP, KIDS_SWIMMING);
    val classSubTypes = List.of(BASIC, BEGINNER);
    val memberLatitude = 33.123456;
    val memberLongitude = 126.123456;
    val page = 1;
    val size = 10;
    val sort = DISTANCE_ASC;
    val response = TestFindSwimmingClassesResponseFactory.create();

    when(useCase.findSwimmingClasses(any())).thenReturn(response);
    // when
    // then
    mockMvc.perform(get(PATH)
            .param("start-date", startDate.toString())
            .param("end-date", endDate.toString())
            .param("start-times", "06:00", "17:00")
            .param("days", "MONDAY", "TUESDAY", "WEDNESDAY")
            .param("class-types", "GROUP", "KIDS_SWIMMING")
            .param("class-sub-types", "BASIC", "BEGINNER")
            .param("member-latitude", String.valueOf(memberLatitude))
            .param("member-longitude", String.valueOf(memberLongitude))
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .param("sort", sort.name()))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));

    verify(useCase, only()).findSwimmingClasses(assertArg(i -> {
      assertThat(i.memberId()).isEqualTo(Long.parseLong(MEMBER_ID));
      assertThat(i.startDate()).isEqualTo(startDate);
      assertThat(i.endDate()).isEqualTo(endDate);
      assertThat(i.startTimes()).isEqualTo(startTimes);
      assertThat(i.days()).isEqualTo(days);
      assertThat(i.classTypes()).isEqualTo(classTypes);
      assertThat(i.classSubTypes()).isEqualTo(classSubTypes);
      assertThat(i.memberLatitude()).isEqualTo(memberLatitude);
      assertThat(i.memberLongitude()).isEqualTo(memberLongitude);
      assertThat(i.pageable().getPageNumber()).isEqualTo(page - 1);
      assertThat(i.pageable().getPageSize()).isEqualTo(size);
      assertThat(i.sort()).isEqualTo(sort);
    }));
  }

  private static class TestFindSwimmingClassesResponseFactory {

    private static FindSwimmingClassesResponse create() {
      val classes = List.of(SwimmingClass.builder()
          .swimmingPoolId(1L)
          .imageUrl("MOCK_IMAGE_URL")
          .isFavorite(true)
          .distance(100)
          .name("MOCK_NAME")
          .address("MOCK_ADDRESS")
          .rating(4.5)
          .reviewCount(100)
          .build());
      return new FindSwimmingClassesResponse(new PageImpl<>(classes));
    }
  }
}