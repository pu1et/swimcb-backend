package com.project.swimcb.bo.faq.application.in;

import com.project.swimcb.bo.faq.adapter.in.UploadFaqFileResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFaqFileUseCase {

  UploadFaqFileResponse uploadFile(@NonNull MultipartFile file);
}
