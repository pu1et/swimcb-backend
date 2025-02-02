package com.project.swimcb.faq.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs/{faqId}")
public class UpdateFaqController {

  @Operation(summary = "FAQ 수정")
  @PutMapping
  public void updateFaq(@RequestBody UpdateFaqRequest request) {
  }
}
