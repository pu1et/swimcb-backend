package com.project.swimcb.bo.faq.adapter.in;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.web.multipart.MultipartFile;

public record UploadFaqFileRequest(
    @Parameter(description = "파일", content = @Content(mediaType = MULTIPART_FORM_DATA_VALUE))
    MultipartFile file
) {

}
