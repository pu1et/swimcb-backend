package com.project.swimcb.faq.adapter.in;

import java.util.List;
import lombok.NonNull;

public record DeleteFaqsRequest(
    @NonNull List<Long> faqIds
) {

}
