package com.project.swimcb.bo.notice.adapter.in;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.swimcb.bo.notice.application.in.UploadBoNoticeFileUseCase;
import com.project.swimcb.common.WebMvcTestWithoutSecurity;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTestWithoutSecurity(controllers = UploadBoNoticeFileController.class)
class UploadBoNoticeFileControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UploadBoNoticeFileUseCase useCase;

  @Autowired
  private ObjectMapper objectMapper;

  private static final String PATH = "/api/bo/notices/files";

  @Test
  @DisplayName("파일 업로드 정상 처리")
  void shouldUploadFileSuccessfully() throws Exception {
    // given
    val file = MultipartFileFactory.create();
    val response = UploadNoticeFileResponseFactory.create();

    when(useCase.uploadFile(any())).thenReturn(response);
    // when
    // then
    mockMvc.perform(multipart(PATH, 1L)
            .file(file)
            .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isOk())
        .andExpect(content().json(objectMapper.writeValueAsString(response)));
  }

  @Test
  @DisplayName("파일이 없으면 400 반환")
  void shouldReturn400WhenFileIsMissing() throws Exception {
    // given
    // when
    // then
    mockMvc.perform(multipart(PATH, 1L)
            .contentType(MULTIPART_FORM_DATA))
        .andExpect(status().isBadRequest());
  }

  private static class MultipartFileFactory {

    private static MockMultipartFile create() {
      return new MockMultipartFile("file", "test.jpg", IMAGE_JPEG_VALUE,
          "test".getBytes());
    }
  }

  private static class UploadNoticeFileResponseFactory {

    private static UploadBoNoticeFileResponse create() {
      return UploadBoNoticeFileResponse.builder()
          .name("test.jpg")
          .path("notice/test.jpg")
          .url("http://host.com/notice/test.jpg")
          .size(4)
          .build();
    }
  }
}
