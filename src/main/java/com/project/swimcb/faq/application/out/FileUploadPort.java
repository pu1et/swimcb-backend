package com.project.swimcb.faq.application.out;

import com.project.swimcb.faq.adapter.out.UploadedFile;
import java.io.IOException;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

public interface FileUploadPort {

  UploadedFile uploadFile(@NonNull String path, @NonNull MultipartFile file) throws IOException;
}
