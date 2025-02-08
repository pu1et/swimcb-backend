package com.project.swimcb.faq.adapter.in;

import com.project.swimcb.faq.application.in.SearchFaqsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs/search")
@RequiredArgsConstructor
public class SearchFaqsController {

  private final SearchFaqsUseCase useCase;

  @Operation(summary = "FAQ 리스트 제목 검색")
  @GetMapping
  public Page<FindFaqsResponse> searchFaqs(
      @Parameter(description = "검색어") @RequestParam(value = "keyword") String keyword,
      @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "1") int page,
      @Parameter(description = "페이지 크기") @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return useCase.searchFaqs(keyword, PageRequest.of(page - 1, size));
  }
}
