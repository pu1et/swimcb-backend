package com.project.swimcb.bo.notice.adapter.in;

import com.project.swimcb.bo.notice.application.in.CreateBoNoticeUseCase;
import com.project.swimcb.bo.notice.domain.CreateBoNoticeCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/notices")
@RequiredArgsConstructor
public class CreateBoNoticeController {

  private final CreateBoNoticeUseCase useCase;

  @Operation(summary = "공지사항 등록")
  @PostMapping
  public void createBoNotice(@Valid @RequestBody CreateBoNoticeRequest request) {
    useCase.createNotice(CreateBoNoticeCommand.from(request));
  }
}
