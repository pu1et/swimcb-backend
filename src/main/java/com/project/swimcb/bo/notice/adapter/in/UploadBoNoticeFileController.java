package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.UploadBoNoticeFileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/notices/files")
@RequiredArgsConstructor
public class UploadBoNoticeFileController {

  private final UploadBoNoticeFileUseCase useCase;

  @Operation(summary = "공지사항 파일 첨부 처리")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UploadBoNoticeFileResponse uploadFile(
      @Valid @ModelAttribute UploadBoNoticeFileRequest request
  ) {
    return useCase.uploadFile(request.file());
  }
}
