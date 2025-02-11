package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.UpdateNoticeUseCase;
import com.project.swimcb.bo.notice.domain.UpdateNoticeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/bo/notices/{noticeId}")
@RequiredArgsConstructor
public class UpdateNoticeController {

  private final UpdateNoticeUseCase useCase;

  @Operation(summary = "공지사항 수정")
  @PutMapping
  public void updateNotice(
      @PathVariable(name = "noticeId") Long noticeId,
      @Valid @RequestBody UpdateNoticeRequest request
  ) {
    useCase.updateNotice(UpdateNoticeCommand.from(noticeId, request));
  }
}
