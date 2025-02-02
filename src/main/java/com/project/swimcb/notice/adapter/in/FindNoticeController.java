package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.notice.domain.Notice;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/notices")
public class FindNoticeController {

  @Operation(summary = "공지사항 리스트 조회")
  @GetMapping
  public List<Notice> findNotices() {
    return List.of(Notice.builder()
            .noticeId(1L)
            .title("공지사항 제목1")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 3))
            .isVisible(true)
            .build(),
        Notice.builder()
            .noticeId(2L)
            .title("공지사항 제목2")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 2))
            .isVisible(false)
            .build(),
        Notice.builder()
            .noticeId(3L)
            .title("공지사항 제목3")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 1))
            .isVisible(true)
            .build());
  }
}
