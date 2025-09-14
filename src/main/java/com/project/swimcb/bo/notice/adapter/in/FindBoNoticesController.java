package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.FindBoNoticesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/notices")
@RequiredArgsConstructor
public class FindBoNoticesController {

  private final FindBoNoticesUseCase useCase;

  @Operation(summary = "공지사항 리스트 조회")
  @GetMapping
  public Page<FindBoNoticesResponse> findNotices(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return useCase.findNotices(PageRequest.of(page - 1, size));
  }
}
