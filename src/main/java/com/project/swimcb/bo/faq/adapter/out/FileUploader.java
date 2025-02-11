package com.project.swimcb.bo.faq.adapter.out;

import com.project.swimcb.config.s3.S3Config;
import com.project.swimcb.bo.faq.application.out.FileUploadPort;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FileUploader implements FileUploadPort {

  private final S3Config s3Config;

  public UploadedFile uploadFile(@NonNull String directory, @NonNull MultipartFile file) throws IOException {

    val s3Client = s3Config.getS3Client();

    val fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    val bucketName = s3Config.getBucketName();
    val filePath = directory + "/" + fileName;

    val putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(filePath)
        .contentType(file.getContentType())
        .build();

    val response = s3Client.putObject(putObjectRequest,
        RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));

    val url = s3Config.getHost() + "/" + bucketName + "/" + filePath;

    if (response.sdkHttpResponse().isSuccessful()) {
      return UploadedFile.builder()
          .name(fileName)
          .path(filePath)
          .url(url)
          .size(file.getSize())
          .build();
    }
    throw new IOException("파일 업로드에 실패하였습니다.");
  }
}
