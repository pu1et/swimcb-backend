package com.project.swimcb.bo.swimmingpool.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UploadSwimmingPoolUsageAgreementFileInteractorTest {

  @InjectMocks
  private UploadSwimmingPoolUsageAgreementFileInteractor interactor;

  @Mock
  private FileUploadPort fileUploadPort;

  @Test
  @DisplayName("파일 업로드 성공")
  void shouldUploadFileSuccessfully() throws IOException {
    // given
    val mockFile = mock(MultipartFile.class);
    val expectedUploadedFile = UploadedFileFactory.create();

    when(fileUploadPort.uploadFile(anyString(), any())).thenReturn(expectedUploadedFile);
    // when
    val response = interactor.uploadSwimmingPoolUsageAgreementFile(mockFile);
    // then
    assertThat(response).isNotNull();
    assertThat(response.name()).isEqualTo(expectedUploadedFile.name());
    assertThat(response.path()).isEqualTo(expectedUploadedFile.path());

    verify(fileUploadPort, only()).uploadFile("swimming-pool-usage-agreement", mockFile);
  }

  @Test
  @DisplayName("파일 업로드 실패시 UncheckedIOException 발생")
  void shouldThrowUncheckedIOExceptionWhenUploadFails() throws IOException {
    // given
    val mockFile = mock(MultipartFile.class);

    when(fileUploadPort.uploadFile(anyString(), any())).thenThrow(new IOException());
    // when
    // then
    assertThatThrownBy(() -> interactor.uploadSwimmingPoolUsageAgreementFile(mockFile))
        .isInstanceOf(UncheckedIOException.class)
        .hasMessage("파일 업로드에 실패하였습니다.");
  }

  @Test
  @DisplayName("Null 파일 입력시 NullPointerException 발생")
  void shouldThrowNullPointerExceptionWhenFileIsNull() {
    // given
    // when
    // then
    assertThatThrownBy(() -> interactor.uploadSwimmingPoolUsageAgreementFile(null))
        .isInstanceOf(NullPointerException.class);
  }

  private static class UploadedFileFactory {

    public static UploadedFile create() {
      return UploadedFile.builder()
          .name("test.jpg")
          .path("faq/test.jpg")
          .url("http://host.com/faq/test.jpg")
          .size(4L)
          .build();
    }
  }
}