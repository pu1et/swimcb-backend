package com.project.swimcb.bo.swimmingpool.adapter.in;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadSwimmingPoolImageRequest(
    @NotNull
    @Parameter(description = "이미지 파일", content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE))
    MultipartFile file
) {

}
