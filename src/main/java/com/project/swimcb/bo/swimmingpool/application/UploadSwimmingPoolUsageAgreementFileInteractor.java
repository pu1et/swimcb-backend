package com.project.swimcb.bo.swimmingpool.application;

import com.project.swimcb.bo.faq.application.out.FileUploadPort;
import com.project.swimcb.bo.swimmingpool.adapter.in.UploadSwimmingPoolUsageAgreementResponse;
import com.project.swimcb.bo.swimmingpool.application.in.UploadSwimmingPoolUsageAgreementFileUseCase;
import java.io.IOException;
import java.io.UncheckedIOException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UploadSwimmingPoolUsageAgreementFileInteractor implements
    UploadSwimmingPoolUsageAgreementFileUseCase {

  private final FileUploadPort fileUploadPort;

  @Override
  public UploadSwimmingPoolUsageAgreementResponse uploadSwimmingPoolUsageAgreementFile(
      @NonNull MultipartFile file) {
    try {
      return UploadSwimmingPoolUsageAgreementResponse.from(
          fileUploadPort.uploadFile("swimming-pool-usage-agreement", file));
    } catch (IOException e) {
      throw new UncheckedIOException("파일 업로드에 실패하였습니다.", e);
    }
  }
}
