package com.project.swimcb.bo.clone.application.port.out;

import com.project.swimcb.bo.clone.adapter.out.FreeSwimmingCopyCandidate;
import java.time.YearMonth;
import java.util.List;
import lombok.NonNull;

public interface CopyFreeSwimmingDsGateway {

  List<FreeSwimmingCopyCandidate> findAllFreeSwimmingsByMonth(@NonNull YearMonth month);

}
