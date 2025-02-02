package com.project.swimcb.faq.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs/files")
public class UploadFaqFileController {

  @Operation(summary = "FAQ 파일 첨부 처리")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UploadFaqFileResponse uploadFile(
      @ModelAttribute UploadFaqFileRequest request
  ) {
    return UploadFaqFileResponse.builder()
        .name("첨부파일1")
        .path("/content/faqs/1")
        .size(1024)
        .build();
  }
}
