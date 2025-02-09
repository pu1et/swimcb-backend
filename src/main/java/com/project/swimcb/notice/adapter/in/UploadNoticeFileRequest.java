package com.project.swimcb.notice.adapter.in;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UploadNoticeFileRequest(
    @NotNull
    @Parameter(description = "파일", content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE))
    MultipartFile file
) {

}
