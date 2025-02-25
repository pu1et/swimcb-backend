package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.bo.faq.application.in.FindFaqsUseCase;
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

@Tag(name = "FAQ")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/faqs")
@RequiredArgsConstructor
public class FindFaqsController {

  private final FindFaqsUseCase useCase;

  @Operation(summary = "FAQ 리스트 조회")
  @GetMapping
  public Page<FindFaqsResponse> findFaqs(
      @RequestParam(value = "page", defaultValue = "1") int page,
      @RequestParam(value = "size", defaultValue = "10") int size
  ) {
    return useCase.findFaqs(PageRequest.of(page - 1, size));
  }
}
