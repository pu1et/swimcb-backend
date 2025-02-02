package com.project.swimcb.faq.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs/{faqId}/is-visible")
public class UpdateFaqIsVisibleController {

  @Operation(summary = "FAQ 활성/비활성 처리")
  @PutMapping
  public void updateFaqIsVisible(@RequestBody UpdateFaqIsVisibleRequest request) {
  }
}
