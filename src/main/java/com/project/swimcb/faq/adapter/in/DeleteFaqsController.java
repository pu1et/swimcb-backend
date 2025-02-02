package com.project.swimcb.faq.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs")
public class DeleteFaqsController {

  @Operation(summary = "FAQ 삭제")
  @DeleteMapping
  public void deleteFaqs(@RequestBody DeleteFaqsRequest request) {
  }
}
