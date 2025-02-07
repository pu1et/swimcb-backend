package com.project.swimcb.faq.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs/search")
public class SearchFaqsController {

  @Operation(summary = "FAQ 리스트 검색")
  @GetMapping
  public List<FindFaqsResponse> searchFaqs(
      @Parameter(description = "검색어") @RequestParam(value = "keyword") String keyword
  ) {
    return List.of(FindFaqsResponse.builder()
            .faqId(1L)
            .title("FAQ 제목1")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 3))
            .isVisible(true)
            .build(),
        FindFaqsResponse.builder()
            .faqId(2L)
            .title("FAQ 제목2")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 2))
            .isVisible(false)
            .build(),
        FindFaqsResponse.builder()
            .faqId(3L)
            .title("FAQ 제목3")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 1))
            .isVisible(true)
            .build());
  }
}
