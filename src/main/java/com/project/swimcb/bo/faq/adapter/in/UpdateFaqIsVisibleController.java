package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.bo.faq.application.in.UpdateFaqIsVisibleUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/bo/faqs/{faqId}/is-visible")
@RequiredArgsConstructor
public class UpdateFaqIsVisibleController {

  private final UpdateFaqIsVisibleUseCase useCase;

  @Operation(summary = "FAQ 활성/비활성 처리")
  @PutMapping
  public void updateFaqIsVisible(
      @PathVariable(name = "faqId") Long faqId,
      @RequestBody UpdateFaqIsVisibleRequest request
  ) {
    useCase.updateFaqIsVisible(faqId, request.isVisible());
  }
}
