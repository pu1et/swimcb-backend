package com.project.swimcb.bo.faq.application;

import com.project.swimcb.bo.faq.adapter.in.UploadFaqFileResponse;
import com.project.swimcb.bo.faq.application.in.UploadFaqFileUseCase;
import com.project.swimcb.bo.faq.application.out.FileUploadPort;
import java.io.IOException;
import java.io.UncheckedIOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadFaqFileInteractor implements UploadFaqFileUseCase {

  private final FileUploadPort fileUploadPort;

  @Override
  public UploadFaqFileResponse uploadFile(@NonNull MultipartFile file) {
    try {
      return UploadFaqFileResponse.from(fileUploadPort.uploadFile("faq", file));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 업로드에 실패하였습니다.", e);
    }
  }
}
