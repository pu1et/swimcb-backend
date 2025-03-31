package com.project.swimcb.swimmingpool.adapter.in;

import com.project.swimcb.swimmingpool.application.in.FindSwimmingClassTicketUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "사용자 - 완료")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/swimming-pools/classes/tickets/{ticketId}")
@RequiredArgsConstructor
public class FindSwimmingClassTicketController {

  private final FindSwimmingClassTicketUseCase useCase;
  private final FindSwimmingClassTicketResponseMapper mapper;

  @Operation(summary = "수영 클래스 예약 - 티켓 정보 조회")
  @GetMapping
  public FindSwimmingClassTicketResponse findSwimmingClassTicket(
      @PathVariable(value = "ticketId") long ticketId
  ) {
    return mapper.toResponse(useCase.findSwimmingClassTicket(ticketId));
  }
}
