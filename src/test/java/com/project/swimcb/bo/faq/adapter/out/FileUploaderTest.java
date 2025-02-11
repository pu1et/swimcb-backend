package com.project.swimcb.bo.faq.adapter.out;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

import com.project.swimcb.bo.faq.adapter.out.FileUploader;
import com.project.swimcb.config.s3.S3Config;
import java.io.IOException;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@ExtendWith(MockitoExtension.class)
class FileUploaderTest {

  @InjectMocks
  private FileUploader fileUploader;

  @Mock
  private S3Client s3Client;

  @Mock
  private S3Config s3Config;

  @Test
  @DisplayName("파일 업로드 성공")
  void shouldUploadFileSuccessfully() throws IOException {
    // given
    val file = new MockMultipartFile("file", "test.jpg", IMAGE_JPEG_VALUE,
        "test".getBytes());

    val host = "http://host.com";
    val bucketName = "bucket";
    val directory = "directory";
    val fileNameExceptUUID = "-" + file.getOriginalFilename();
    val expectedFilePathExceptFileName = host + "/" + bucketName + "/" + directory + "/";

    val response = PutObjectResponse.builder()
        .sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build()).build();

    when(s3Config.getS3Client()).thenReturn(s3Client);
    when(s3Config.getHost()).thenReturn(host);
    when(s3Config.getBucketName()).thenReturn(bucketName);
    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn((PutObjectResponse) response);
    // when
    val result = fileUploader.uploadFile(directory, file);
    // then
    assertThat(result).isNotNull();
    assertThat(result.name()).contains(fileNameExceptUUID);
    assertThat(result.path()).contains(directory, fileNameExceptUUID);
    assertThat(result.url()).contains(host, expectedFilePathExceptFileName, fileNameExceptUUID);
    assertThat(result.size()).isEqualTo(file.getSize());

    verify(s3Client, only()).putObject((PutObjectRequest) assertArg(i -> {
      val req = (PutObjectRequest) i;
      assertThat(req.bucket()).isEqualTo(bucketName);
      assertThat(req.key()).contains(directory);
      assertThat(req.key()).contains(fileNameExceptUUID);
      assertThat(req.contentType()).isEqualTo(file.getContentType());
    }), any(RequestBody.class));
  }

  @Test
  @DisplayName("파일 업로드 실패시 IOException 발생")
  void shouldThrowExceptionWhenUploadFails() {
    // given
    val file = new MockMultipartFile("file", "test.jpg", IMAGE_JPEG_VALUE,
        "test".getBytes());
    val bucketName = "bucket";
    val path = "path";
    val fileNameExceptUUID = "-" + file.getOriginalFilename();

    val response = PutObjectResponse.builder()
        .sdkHttpResponse(SdkHttpResponse.builder().statusCode(500).build()).build();

    when(s3Config.getS3Client()).thenReturn(s3Client);
    when(s3Config.getBucketName()).thenReturn(bucketName);
    when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
        .thenReturn((PutObjectResponse) response);
    // when
    // then
    assertThatThrownBy(() -> fileUploader.uploadFile(path, file))
        .isInstanceOf(IOException.class)
        .hasMessage("파일 업로드에 실패하였습니다.");

    verify(s3Client, only()).putObject((PutObjectRequest) assertArg(i -> {
      val req = (PutObjectRequest) i;
      assertThat(req.bucket()).isEqualTo(bucketName);
      assertThat(req.key()).contains(path);
      assertThat(req.key()).contains(fileNameExceptUUID);
      assertThat(req.contentType()).isEqualTo(file.getContentType());
    }), any(RequestBody.class));
  }
}