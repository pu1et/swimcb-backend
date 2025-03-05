package com.project.swimcb.bo.swimmingpool.application.in;

import com.project.swimcb.bo.swimmingpool.adapter.in.UploadSwimmingPoolUsageAgreementResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface UploadSwimmingPoolUsageAgreementFileUseCase {

  UploadSwimmingPoolUsageAgreementResponse uploadSwimmingPoolUsageAgreementFile(
      @NonNull MultipartFile file);
}
