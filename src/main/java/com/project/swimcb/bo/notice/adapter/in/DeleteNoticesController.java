package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.DeleteNoticesUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/notices")
@RequiredArgsConstructor
public class DeleteNoticesController {

  private final DeleteNoticesUseCase useCase;

  @Operation(summary = "공지사항 리스트 삭제")
  @DeleteMapping
  public void deleteNotices(@RequestBody DeleteNoticeRequest request) {
  }
}
