package com.project.swimcb.notice.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/notices/files")
public class UploadFileController {

  @Operation(summary = "공지사항 파일 첨부 처리")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UploadNoticeFileResponse uploadFile(
      @ModelAttribute UploadNoticeFileRequest request
  ) {
    return UploadNoticeFileResponse.builder()
        .name("첨부파일1")
        .path("/content/notices/1")
        .size(1024)
        .build();
  }
}
