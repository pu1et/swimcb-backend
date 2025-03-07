package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.UpdateNoticeIsVisibleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/notices/{noticeId}/is-visible")
@RequiredArgsConstructor
public class UpdateNoticeIsVisibleController {

  private final UpdateNoticeIsVisibleUseCase useCase;

  @Operation(summary = "공지사항 활성/비활성 처리")
  @PutMapping
  public void updateNoticeIsVisible(
      @PathVariable(name = "noticeId") Long noticeId,
      @RequestBody UpdateNoticeIsVisibleRequest request
  ) {
    useCase.updateNoticeIsVisible(noticeId, request.isVisible());
  }
}
