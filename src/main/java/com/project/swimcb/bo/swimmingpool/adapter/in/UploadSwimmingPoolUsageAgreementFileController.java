package com.project.swimcb.bo.swimmingpool.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/swimming-pools/files/usage-agreements")
@RequiredArgsConstructor
public class UploadSwimmingPoolUsageAgreementFileController {

  @Operation(summary = "수영장 이용 동의서 파일 첨부 처리")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UploadSwimmingPoolUsageAgreementResponse uploadFile(
      @Valid @ModelAttribute UploadSwimmingPoolUsageAgreementRequest request
  ) {
    return UploadSwimmingPoolUsageAgreementResponse.builder()
        .name("usage-agreement.txt")
        .path("/swimming-pool/usage-agreement/usage-agreement.txt")
        .url("http://host.com/swimming-pool/usage-agreement/usage-agreement.txt")
        .size(1024)
        .build();
  }
}
