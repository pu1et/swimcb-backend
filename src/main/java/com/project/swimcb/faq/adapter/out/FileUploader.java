package com.project.swimcb.faq.adapter.out;

import com.project.swimcb.config.s3.S3Config;
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
public class FileUploader {

  private final S3Config s3Config;

  public String uploadFile(@NonNull String path, @NonNull MultipartFile file) throws IOException {


    val s3Client = s3Config.getS3Client();

    val fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    val filePath = path + "/" + fileName;

    val putObjectRequest = PutObjectRequest.builder()
        .bucket(s3Config.getBucketName())
        .key(filePath)
        .contentType(file.getContentType())
        .build();

    val response = s3Client.putObject(putObjectRequest,
        RequestBody.fromByteBuffer(ByteBuffer.wrap(file.getBytes())));

    if (response.sdkHttpResponse().isSuccessful()) {
      return filePath;
    }
    throw new IOException("파일 업로드에 실패하였습니다.");
  }
}
