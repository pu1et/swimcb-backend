package com.project.swimcb.notice.adapter.in;

import com.project.swimcb.notice.application.in.FindNoticesUseCase;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class FindNoticesController {

  private final FindNoticesUseCase useCase;

  @Operation(summary = "공지사항 리스트 조회")
  @GetMapping
  public Page<FindNoticesResponse> findNotices(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return useCase.findNotices(PageRequest.of(page - 1, size));
  }
}
