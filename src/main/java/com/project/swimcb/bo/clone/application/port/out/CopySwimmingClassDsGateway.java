package com.project.swimcb.bo.clone.application.port.out;

import com.project.swimcb.bo.clone.domain.SwimmingClassCopyCandidate;
import java.time.YearMonth;
import java.util.List;
import lombok.NonNull;

public interface CopySwimmingClassDsGateway {

  List<SwimmingClassCopyCandidate> findAllSwimmingClassesByMonth(@NonNull YearMonth month);

  void deleteSwimmingClassByMonth(@NonNull YearMonth month);

}
