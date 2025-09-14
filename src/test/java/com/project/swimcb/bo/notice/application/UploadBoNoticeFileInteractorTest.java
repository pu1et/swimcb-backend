package com.project.swimcb.bo.notice.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import com.project.swimcb.bo.faq.adapter.out.UploadedFile;
import com.project.swimcb.bo.faq.application.out.FileUploadPort;
import java.io.IOException;
import java.io.UncheckedIOException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class UploadBoNoticeFileInteractorTest {
  
  @InjectMocks
  private UploadBoNoticeFileInteractor interactor;

  @Mock
  private FileUploadPort fileUploadPort;

  @Test
  @DisplayName("파일 업로드 성공")
  void shouldUploadFileSuccessfully() throws IOException {
    // given
    val file = MultipartFileFactory.create();
    val uploadedFile = UploadedFileFactory.create();

    when(fileUploadPort.uploadFile(anyString(), any())).thenReturn(uploadedFile);
    // when
    val response = interactor.uploadFile(file);
    // then
    assertThat(response).isNotNull();
    assertThat(response.name()).isEqualTo(uploadedFile.name());
    assertThat(response.path()).isEqualTo(uploadedFile.path());
    assertThat(response.url()).isEqualTo(uploadedFile.url());
    assertThat(response.size()).isEqualTo(uploadedFile.size());

    verify(fileUploadPort, only()).uploadFile("notice", file);
  }

  @Test
  @DisplayName("파일 업로드 실패시 UncheckedIOException 발생")
  void shouldThrowUncheckedIOExceptionWhenUploadFails() throws IOException {
    // given
    val file = MultipartFileFactory.create();

    when(fileUploadPort.uploadFile(anyString(), any())).thenThrow(new IOException());
    // when
    // then
    assertThatThrownBy(() -> interactor.uploadFile(file))
        .isInstanceOf(UncheckedIOException.class)
        .hasMessage("파일 업로드에 실패하였습니다.");
  }

  private static class MultipartFileFactory {

    private static MockMultipartFile create() {
      return new MockMultipartFile("file", "test.jpg", IMAGE_JPEG_VALUE,
          "test".getBytes());
    }
  }

  private static class UploadedFileFactory {

    private static UploadedFile create() {
      return UploadedFile.builder()
          .name("test.jpg")
          .path("notice/test.jpg")
          .url("http://host.com/notice/test.jpg")
          .size(4L)
          .build();
    }
  }
}
