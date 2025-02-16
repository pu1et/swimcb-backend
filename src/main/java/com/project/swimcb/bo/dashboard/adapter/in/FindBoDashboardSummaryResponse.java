package com.project.swimcb.bo.dashboard.adapter.in;

import lombok.Builder;

@Builder
public record FindBoDashboardSummaryResponse(
    int reservationRequestCount,
    int cancellationProcessedCount,
    int refundProcessedCount) {

}
