package com.project.swimcb.bo.swimmingclass.adapter.in;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassTypesRequest.SubType;
import com.project.swimcb.bo.swimmingclass.adapter.in.UpdateBoSwimmingClassTypesRequest.Type;
import com.project.swimcb.config.security.SecurityConfig;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UpdateBoSwimmingClassTypesController.class)
@Import(SecurityConfig.class)
class UpdateBoSwimmingClassTypesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/swimming-classes/types";

  @Test
  @DisplayName("강습형태/구분 리스트 일괄 업데이트 성공")
  void shouldUpdateSuccessfully() throws Exception {
    // given
    val request = UpdateBoSwimmingClassTypesRequestFactory.create();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("강습형태 리스트가 6개 미만일 경우 400 반환")
  void shouldReturn400WhenTypeSizeLessThan6() throws Exception {
    // given
    val request = UpdateBoSwimmingClassTypesRequestFactory.create(5);
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습형태는 6개로 고정되어 있습니다.")));
  }

  @Test
  @DisplayName("강습형태 리스트가 6개 초과일 경우 400 반환")
  void shouldReturn400WhenTypeSizeGreaterThan6() throws Exception {
    // given
    val request = UpdateBoSwimmingClassTypesRequestFactory.create(7);
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습형태는 6개로 고정되어 있습니다.")));
  }

  @Test
  @DisplayName("강습형태의 이름이 null일 경우 400 반환")
  void shouldReturn400WhenTypeNameIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassTypesRequestFactory.typeNameIsNull();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습형태 이름은 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습구분 리스트가 null일 경우 400 반환")
  void shouldReturn400WhenSubTypeIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassTypesRequestFactory.subTypesIsNull();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습구분 리스트는 null이 될 수 없습니다.")));
  }

  @Test
  @DisplayName("강습구분 리스트가 6개 초과일 경우 400 반환")
  void shouldReturn400WhenSubTypeIsGreaterThan6() throws Exception {
    // given
    val request = UpdateBoSwimmingClassTypesRequestFactory.subTypeIsGreaterThan6();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습구분 리스트는 6개 이하로만 설정할 수 있습니다.")));
  }

  @Test
  @DisplayName("강습구분 이름이 null일 경우 400 반환")
  void shouldReturn400WhenSubTypeNameIsNull() throws Exception {
    // given
    val request = UpdateBoSwimmingClassTypesRequestFactory.subTypeNameIsNull();
    // when
    // then
    mockMvc.perform(put(PATH)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(content().string(containsString("강습구분 이름은 null이 될 수 없습니다.")));
  }

  private static class UpdateBoSwimmingClassTypesRequestFactory {

    private static UpdateBoSwimmingClassTypesRequest create() {
      return create(5, Stream.of(typeWithSubType(6L, subTypes(1))));
    }

    private static UpdateBoSwimmingClassTypesRequest create(int count,
        Stream<Type> additionalTypes) {
      val types = Stream.concat(
          IntStream.range(0, count).mapToObj(i -> typeWithNoSub(i + 1L)),
          additionalTypes
      ).toList();
      return UpdateBoSwimmingClassTypesRequest.builder().types(types).build();
    }

    private static UpdateBoSwimmingClassTypesRequest create(int count) {
      return create(count, Stream.empty());
    }

    private static UpdateBoSwimmingClassTypesRequest typeNameIsNull() {
      return create(5, Stream.of(Type.builder().typeId(6L).subTypes(List.of()).build()));
    }

    private static UpdateBoSwimmingClassTypesRequest subTypesIsNull() {
      return create(5, Stream.of(typeWithSubType(6L, null)));
    }

    private static UpdateBoSwimmingClassTypesRequest subTypeIsGreaterThan6() {
      val subTypes = subTypes(7);
      return create(5, Stream.of(typeWithSubType(6L, subTypes)));
    }

    private static UpdateBoSwimmingClassTypesRequest subTypeNameIsNull() {
      return create(5,
          Stream.of(typeWithSubType(6L, List.of(SubType.builder().subTypeId(1L).build()))));
    }

    private static Type typeWithSubType(long typeId, List<SubType> subTypes) {
      return Type.builder().typeId(typeId).name("MOCK_NAME").subTypes(subTypes).build();
    }

    private static Type typeWithNoSub(long typeId) {
      return Type.builder().typeId(typeId).name("MOCK_NAME").subTypes(List.of()).build();
    }

    private static SubType subType(long subTypeId) {
      return SubType.builder().subTypeId(subTypeId).name("MOCK_NAME").build();
    }

    private static List<SubType> subTypes(int count) {
      return IntStream.range(0, count)
          .mapToObj(i -> SubType.builder().subTypeId(i + 1L).name("MOCK_NAME").build())
          .toList();
    }
  }
}