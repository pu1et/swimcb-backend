package com.project.swimcb.faq.adapter.in;

import com.project.swimcb.faq.domain.FaqSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs")
public class FindFaqsController {

  @Operation(summary = "FAQ 리스트 조회")
  @GetMapping
  public List<FaqSummary> findFaqs() {
    return List.of(FaqSummary.builder()
            .faqId(1L)
            .title("FAQ 제목1")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 3))
            .isVisible(true)
            .build(),
        FaqSummary.builder()
            .faqId(2L)
            .title("FAQ 제목2")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 2))
            .isVisible(false)
            .build(),
        FaqSummary.builder()
            .faqId(3L)
            .title("FAQ 제목3")
            .createdBy("운영자")
            .createdAt(LocalDate.of(2025, 1, 1))
            .isVisible(true)
            .build());
  }
}
