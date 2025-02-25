package com.project.swimcb.bo.reservation.adapter.in;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BO")
@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/api/bo/payments/{paymentId}/approve")
public class BoApprovePaymentController {

  @Operation(summary = "결제 승인 처리")
  @PostMapping
  public void approvePayment(@PathVariable(value = "paymentId") long paymentId) {

  }
}
