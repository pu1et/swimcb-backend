package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.FindNoticeDetailUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/bo/notices/{noticeId}")
@RequiredArgsConstructor
public class FindNoticeDetailController {

  private final FindNoticeDetailUseCase useCase;

  @Operation(summary = "공지사항 상세 조회")
  @GetMapping
  public FindNoticeDetailResponse findNotices(
      @PathVariable(value = "noticeId") long noticeId
  ) {
    return useCase.findDetail(noticeId);
  }
}
