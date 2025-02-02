package com.project.swimcb.notice.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/notices")
public class RegisterNoticeController {

  @Operation(summary = "공지사항 등록")
  @PostMapping
  public void registerNotice(@RequestBody RegisterNoticeRequest request) {
  }
}
