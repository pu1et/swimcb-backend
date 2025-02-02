package com.project.swimcb.faq.adapter.in;

import com.project.swimcb.faq.domain.FaqDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs/{faqId}")
public class FindFaqDetailController {

  @Operation(summary = "FAQ 상세 조회")
  @GetMapping
  public FaqDetail findFaq(
      @PathVariable(value = "faqId") long faqId
  ) {
    return FaqDetail.builder()
        .createdBy("운영자")
        .title("FAQ 제목1")
        .content("FAQ 내용1")
        .imageUrls(List.of("https://ibb.co/bjGKF8WV", "https://ibb.co/bjGKF8WV"))
        .isVisible(true)
        .createdAt(LocalDate.of(2025, 1, 1))
        .build();
  }
}
