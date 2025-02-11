package com.project.swimcb.bo.notice.application.in;

import com.project.swimcb.bo.notice.adapter.in.UploadNoticeFileResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface UploadNoticeFileUseCase {

  UploadNoticeFileResponse uploadFile(@NonNull MultipartFile file);
}
