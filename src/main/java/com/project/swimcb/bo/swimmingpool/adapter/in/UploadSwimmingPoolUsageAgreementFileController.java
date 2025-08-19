package com.project.swimcb.bo.swimmingpool.adapter.in;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.project.swimcb.bo.swimmingpool.application.in.UploadSwimmingPoolUsageAgreementFileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/swimming-pools/files/usage-agreements")
@RequiredArgsConstructor
public class UploadSwimmingPoolUsageAgreementFileController {

  private final UploadSwimmingPoolUsageAgreementFileUseCase useCase;

  @Operation(summary = "수영장 이용 동의서 파일 첨부 처리")
  @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
  public UploadSwimmingPoolUsageAgreementResponse uploadFile(
      @Valid @ModelAttribute UploadSwimmingPoolUsageAgreementRequest request
  ) {
    return useCase.uploadSwimmingPoolUsageAgreementFile(request.file());
  }
}
