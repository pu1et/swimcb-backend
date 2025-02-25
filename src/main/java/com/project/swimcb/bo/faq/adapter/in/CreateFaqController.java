package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.bo.faq.application.in.CreateFaqUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/faqs")
@RequiredArgsConstructor
public class CreateFaqController {

  private final CreateFaqUseCase useCase;

  @Operation(summary = "FAQ 등록")
  @PostMapping
  public void createFaq(@RequestBody CreateFaqRequest request) {
    useCase.createFaq(request.toDomain());
  }
}
