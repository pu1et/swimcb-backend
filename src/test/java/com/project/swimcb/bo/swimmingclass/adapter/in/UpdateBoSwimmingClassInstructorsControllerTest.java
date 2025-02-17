package com.project.swimcb.bo.swimmingclass.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassInstructorsRequest.Instructor;
import com.project.swimcb.config.security.SecurityConfig;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UpdateBoSwimmingClassInstructorsController.class)
@Import(SecurityConfig.class)
class UpdateBoSwimmingClassInstructorsControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/instructors";

  @Test
  @DisplayName("강사 리스트 일괄 업데이트 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    val request = UpdateBoSwimmingClassInstructorsRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("강사 리스트가 null일 경우 400 반환")
  void shouldReturn400WhenInstructorsIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassInstructorsRequestFactory.instructorsIsNull();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강사 리스트는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강사 이름이 null일 경우 400 반환")
  void shouldReturn400WhenInstructorNameIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassInstructorsRequestFactory.instructorNameIsNull();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강사 이름은 null이 될 수 없습니다.")));
  }

  private static class UpdateBoSwimmingClassInstructorsRequestFactory {

    private static UpdateBoSwimmingClassInstructorsRequest create() {
      return UpdateBoSwimmingClassInstructorsRequest.builder()
          .instructors(List.of(
              Instructor.builder()
                  .instructorId(1L)
                  .name("김민수")
                  .build(),
              Instructor.builder()
                  .name("신수진")
                  .build()
          ))
          .build();
    }

    private static UpdateBoSwimmingClassInstructorsRequest instructorsIsNull() {
      return UpdateBoSwimmingClassInstructorsRequest.builder().build();
    }

    private static UpdateBoSwimmingClassInstructorsRequest instructorNameIsNull() {
      return UpdateBoSwimmingClassInstructorsRequest.builder()
          .instructors(List.of(
              Instructor.builder()
                  .instructorId(1L)
                  .name("김민수")
                  .build(),
              Instructor.builder().build()
          ))
          .build();
    }
  }
}