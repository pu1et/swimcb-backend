package com.project.swimcb.notice.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/notices/{noticeId}")
public class FindNoticeDetailController {

  @Operation(summary = "공지사항 상세 조회")
  @GetMapping
  public NoticeDetail findNotices(
      @PathVariable(value = "noticeId") long noticeId
  ) {
    return NoticeDetail.builder()
        .createdBy("운영자")
        .title("공지사항 제목1")
        .content("공지사항 내용1")
        .imageUrls(List.of("https://ibb.co/bjGKF8WV", "https://ibb.co/bjGKF8WV"))
        .isVisible(true)
        .createdAt(LocalDate.of(2025, 1, 1))
        .build();
  }
}
