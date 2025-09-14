package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.UpdateBoNoticeUseCase;
import com.project.swimcb.bo.notice.domain.UpdateBoNoticeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/notices/{noticeId}")
@RequiredArgsConstructor
public class UpdateBoNoticeController {

  private final UpdateBoNoticeUseCase useCase;

  @Operation(summary = "공지사항 수정")
  @PutMapping
  public void updateNotice(
      @PathVariable(name = "noticeId") Long noticeId,
      @Valid @RequestBody UpdateBoNoticeRequest request
  ) {
    useCase.updateNotice(UpdateBoNoticeCommand.from(noticeId, request));
  }
}
