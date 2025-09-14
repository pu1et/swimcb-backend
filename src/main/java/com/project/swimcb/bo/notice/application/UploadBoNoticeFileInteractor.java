package com.project.swimcb.bo.notice.application;

import com.project.swimcb.bo.notice.adapter.in.UploadBoNoticeFileResponse;
import com.project.swimcb.bo.faq.application.out.FileUploadPort;
import com.project.swimcb.bo.notice.application.in.UploadBoNoticeFileUseCase;
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
public class UploadBoNoticeFileInteractor implements UploadBoNoticeFileUseCase {

  private final FileUploadPort fileUploadPort;

  @Override
  public UploadBoNoticeFileResponse uploadFile(@NonNull MultipartFile file) {
    try {
      return UploadBoNoticeFileResponse.from(fileUploadPort.uploadFile("notice", file));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 업로드에 실패하였습니다.", e);
    }
  }
}
