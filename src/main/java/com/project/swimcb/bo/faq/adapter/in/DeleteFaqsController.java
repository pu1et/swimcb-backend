package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.bo.faq.application.in.DeleteFaqsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/bo/faqs")
@RequiredArgsConstructor
public class DeleteFaqsController {

  private final DeleteFaqsUseCase useCase;

  @Operation(summary = "FAQ 리스트 삭제")
  @DeleteMapping
  public void deleteFaqs(@RequestBody DeleteFaqsRequest request) {
    useCase.deleteAll(request.faqIds());
  }
}
