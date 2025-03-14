package com.project.swimcb.bo.faq.adapter.in;

import com.project.swimcb.bo.faq.application.in.UpdateFaqUseCase;
import com.project.swimcb.bo.faq.domain.UpdateFaqCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FAQ")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/faqs/{faqId}")
@RequiredArgsConstructor
public class UpdateFaqController {

  private final UpdateFaqUseCase useCase;

  @Operation(summary = "FAQ 수정")
  @PutMapping
  public void updateFaq(
      @PathVariable(name = "faqId") Long faqId,
      @Valid @RequestBody UpdateFaqRequest request
  ) {
    useCase.updateFaq(UpdateFaqCommand.from(faqId, request));
  }
}
