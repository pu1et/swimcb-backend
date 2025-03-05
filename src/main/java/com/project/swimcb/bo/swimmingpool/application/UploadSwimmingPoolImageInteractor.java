package com.project.swimcb.bo.swimmingpool.application;

import com.project.swimcb.bo.faq.application.out.FileUploadPort;
import com.project.swimcb.bo.swimmingpool.adapter.in.UploadSwimmingPoolImageResponse;
import com.project.swimcb.bo.swimmingpool.application.in.UploadSwimmingPoolImageUseCase;
import java.io.IOException;
import java.io.UncheckedIOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
class UploadSwimmingPoolImageInteractor implements UploadSwimmingPoolImageUseCase {

  private final FileUploadPort fileUploadPort;

  @Override
  public UploadSwimmingPoolImageResponse uploadSwimmingPoolImage(@NonNull MultipartFile file) {
    try {
      return UploadSwimmingPoolImageResponse.from(fileUploadPort.uploadFile("swimming-pool", file));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 업로드에 실패하였습니다.", e);
    }
  }
}
