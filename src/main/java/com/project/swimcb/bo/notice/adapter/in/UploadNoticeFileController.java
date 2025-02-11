package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.UploadNoticeFileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/bo/notices/files")
@RequiredArgsConstructor
public class UploadNoticeFileController {

  private final UploadNoticeFileUseCase useCase;

  @Operation(summary = "공지사항 파일 첨부 처리")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UploadNoticeFileResponse uploadFile(
      @Valid @ModelAttribute UploadNoticeFileRequest request
  ) {
    return useCase.uploadFile(request.file());
  }
}
