package com.project.swimcb.faq.adapter.in;

import com.project.swimcb.notice.adapter.in.RegisterNoticeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@RestController
@RequestMapping("/api/faqs")
public class RegisterFaqController {

  @Operation(summary = "FAQ 등록")
  @PostMapping
  public void registerFaq(@RequestBody RegisterNoticeRequest request) {
  }
}
