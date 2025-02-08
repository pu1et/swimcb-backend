package com.project.swimcb.faq.application.in;

import com.project.swimcb.faq.adapter.in.UploadFaqFileResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFaqFileUseCase {

  UploadFaqFileResponse uploadFile(@NonNull MultipartFile file);
}
