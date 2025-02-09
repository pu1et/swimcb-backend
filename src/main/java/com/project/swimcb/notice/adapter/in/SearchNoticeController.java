package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.notice.application.in.SearchNoticesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/notices/search")
@RequiredArgsConstructor
public class SearchNoticeController {

  private final SearchNoticesUseCase useCase;

  @Operation(summary = "공지사항 리스트 제목 검색")
  @GetMapping
  public Page<FindNoticesResponse> searchNotices(
      @Parameter(description = "검색어") @RequestParam(value = "keyword") String keyword,
      @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
      @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return useCase.searchNotices(keyword, PageRequest.of(page - 1, size));
  }
}
