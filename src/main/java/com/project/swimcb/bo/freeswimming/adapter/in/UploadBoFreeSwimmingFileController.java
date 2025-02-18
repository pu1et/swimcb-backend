package com.project.swimcb.bo.freeswimming.adapter.in;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@RestController
@RequestMapping("/api/bo/free-swimming/files")
@RequiredArgsConstructor
public class UploadBoFreeSwimmingFileController {

  @Operation(summary = "자유수영 데이터 관리 - 자유수영 이미지 파일 첨부 처리")
  @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
  public void uploadBoSwimmingClassFile(
      @Valid @RequestBody UploadBoFreeSwimmingFileRequest request
  ) {
  }
}
