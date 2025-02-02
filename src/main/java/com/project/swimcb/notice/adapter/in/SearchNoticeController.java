package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.notice.domain.NoticeSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/notices/search")
public class SearchNoticeController {

  @Operation(summary = "공지사항 리스트 검색")
  @GetMapping
  public List<NoticeSummary> searchNotices(
      @Parameter(description = "검색어") @RequestParam(value = "keyword") String keyword
  ) {
    return List.of(NoticeSummary.builder()
            .noticeId(1L)
            .title("공지사항 제목1")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 3))
            .isVisible(true)
            .build(),
        NoticeSummary.builder()
            .noticeId(2L)
            .title("공지사항 제목2")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 2))
            .isVisible(false)
            .build(),
        NoticeSummary.builder()
            .noticeId(3L)
            .title("공지사항 제목3")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 1))
            .isVisible(true)
            .build());
  }
}
