package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.bo.faq.application.in.UploadFaqFileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/bo/faqs/files")
@RequiredArgsConstructor
public class UploadFaqFileController {

  private final UploadFaqFileUseCase useCase;

  @Operation(summary = "FAQ 파일 첨부 처리")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UploadFaqFileResponse uploadFile(
      @ModelAttribute UploadFaqFileRequest request
  ) {
    return useCase.uploadFile(request.file());
  }
}
