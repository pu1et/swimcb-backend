package com.project.swimcb.notice.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/notices")
public class DeleteNoticeController {

  @Operation(summary = "공지사항 삭제")
  @DeleteMapping
  public void deleteNotices(@RequestBody DeleteNoticeRequest request) {
  }
}
