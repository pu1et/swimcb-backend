package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.CreateNoticeUseCase;
import com.project.swimcb.bo.notice.domain.CreateNoticeCommand;
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
public class CreateNoticeController {

  private final CreateNoticeUseCase useCase;

  @Operation(summary = "공지사항 등록")
  @PostMapping
  public void createNotice(@Valid @RequestBody CreateNoticeRequest request) {
    useCase.createNotice(CreateNoticeCommand.from(request));
  }
}
