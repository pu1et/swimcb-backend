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
@RequestMapping("/api/bo/swimming-pools/files/images")
@RequiredArgsConstructor
public class UploadSwimmingPoolImageController {

  @Operation(summary = "수영장 대표 이미지 첨부 처리")
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public UploadSwimmingPoolImageResponse uploadFile(
      @Valid @ModelAttribute UploadSwimmingPoolImageRequest request
  ) {
    return UploadSwimmingPoolImageResponse.builder()
        .name("image.jpg")
        .path("/swimming-pool/image.jpg")
        .url("http://host.com/swimming-pool/image.jpg")
        .size(1024)
        .build();
  }
}
