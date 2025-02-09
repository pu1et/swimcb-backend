package com.project.swimcb.notice.application;

import com.project.swimcb.faq.application.out.FileUploadPort;
import com.project.swimcb.notice.adapter.in.UploadNoticeFileResponse;
import com.project.swimcb.notice.application.in.UploadNoticeFileUseCase;
import java.io.IOException;
import java.io.UncheckedIOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadNoticeFileInteractor implements UploadNoticeFileUseCase {

  private final FileUploadPort fileUploadPort;

  @Override
  public UploadNoticeFileResponse uploadFile(@NonNull MultipartFile file) {
    try {
      return UploadNoticeFileResponse.from(fileUploadPort.uploadFile("notice", file));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 업로드에 실패하였습니다.", e);
    }
  }
}
