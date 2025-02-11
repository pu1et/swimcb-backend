package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.RegisterNoticeUseCase;
import com.project.swimcb.bo.notice.domain.RegisterNoticeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "공지사항")
@RestController
@RequestMapping("/api/bo/notices")
@RequiredArgsConstructor
public class RegisterNoticeController {

  private final RegisterNoticeUseCase useCase;

  @Operation(summary = "공지사항 등록")
  @PostMapping
  public void registerNotice(@Valid @RequestBody RegisterNoticeRequest request) {
    useCase.registerNotice(RegisterNoticeCommand.from(request));
  }
}
