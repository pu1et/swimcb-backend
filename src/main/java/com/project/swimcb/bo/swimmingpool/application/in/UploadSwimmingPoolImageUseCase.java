package com.project.swimcb.bo.swimmingpool.application.in;

import com.project.swimcb.bo.swimmingpool.adapter.in.UploadSwimmingPoolImageResponse;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface UploadSwimmingPoolImageUseCase {

  UploadSwimmingPoolImageResponse uploadSwimmingPoolImage(@NonNull MultipartFile file);
}
