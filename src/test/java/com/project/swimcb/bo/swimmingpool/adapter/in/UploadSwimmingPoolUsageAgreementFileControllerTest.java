package com.project.swimcb.bo.swimmingpool.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.swimmingpool.application.in.UploadSwimmingPoolUsageAgreementFileUseCase;
import com.project.swimcb.config.exception.GlobalExceptionHandler;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class UploadSwimmingPoolUsageAgreementFileControllerTest {

  @InjectMocks
  private UploadSwimmingPoolUsageAgreementFileController controller;

  @Mock
  private UploadSwimmingPoolUsageAgreementFileUseCase useCase;

  private final ObjectMapper objectMapper = new ObjectMapper();

  private MockMvc mockMvc;

  private static final String PATH = "/api/bo/swimming-pools/files/usage-agreements";

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller)
        .setControllerAdvice(new GlobalExceptionHandler())
        .build();
  }

  @Test
  @DisplayName("유효한 파일을 업로드하면 성공적으로 응답을 반환한다.")
  void shouldUploadFileSuccessfully() throws Exception {
    // given
    val mockFile = new MockMultipartFile(
        "file",
        "test.jpg",
        IMAGE_JPEG_VALUE,
        "test".getBytes());

    val expectedResponse = UploadSwimmingPoolUsageAgreementResponse.builder()
        .name("test.jpg")
        .path("/path/to/test.jpg")
        .url("http://localhost:8080/path/to/test.jpg")
        .size(4L)
        .build();

    when(useCase.uploadSwimmingPoolUsageAgreementFile(any())).thenReturn(expectedResponse);
    // when
    // then
    mockMvc.perform(multipart(PATH)
            .file(mockFile)
            .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

    verify(useCase, only()).uploadSwimmingPoolUsageAgreementFile(mockFile);
  }

  @Test
  @DisplayName("업로드할 파일이 없는 경우 400을 반환한다.")
  void shouldReturn400WhenFileIsMissing() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(multipart(PATH)
            .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("허용되지 않는 파일 형식을 업로드하면 400을 반환한다.")
  void shouldReturn400WhenFileIsNotSupported() throws Exception {
    // given
    val mockFile = new MockMultipartFile(
        "file",
        "test.txt",
        TEXT_PLAIN_VALUE,
        "test".getBytes());
    // when
    // then
    mockMvc.perform(multipart(PATH)
            .file(mockFile)
            .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }
}