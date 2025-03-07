package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.bo.faq.application.in.FindFaqDetailUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/faqs/{faqId}")
@RequiredArgsConstructor
public class FindFaqDetailController {

  private final FindFaqDetailUseCase useCase;

  @Operation(summary = "FAQ 상세 조회")
  @GetMapping
  public FindFaqDetailResponse findFaq(
      @PathVariable(value = "faqId") long faqId
  ) {
    return useCase.findDetail(faqId);
  }
}
