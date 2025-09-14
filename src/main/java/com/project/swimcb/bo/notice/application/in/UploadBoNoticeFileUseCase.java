package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.adapter.in.UploadBoNoticeFileResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface UploadBoNoticeFileUseCase {

  UploadBoNoticeFileResponse uploadFile(@NonNull MultipartFile file);
}
