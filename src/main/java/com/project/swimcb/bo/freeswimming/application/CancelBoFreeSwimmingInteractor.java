package com.project.swimcb.bo.freeswimming.application;

import com.project.swimcb.bo.freeswimming.domain.CancelBoFreeSwimmingCommand;
import com.project.swimcb.bo.freeswimming.application.port.in.CancelBoFreeSwimmingUseCase;
import com.project.swimcb.db.entity.FreeSwimmingEntity;
import com.project.swimcb.db.repository.FreeSwimmingRepository;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class CancelBoFreeSwimmingInteractor implements CancelBoFreeSwimmingUseCase {

  private final FreeSwimmingRepository freeSwimmingRepository;

  @Override
  public void cancelBoFreeSwimming(@NonNull CancelBoFreeSwimmingCommand command) {
    val freeSwimming = findFreeSwimming(command);

    freeSwimming.cancel(command.cancelReason());
  }

  private FreeSwimmingEntity findFreeSwimming(@NonNull CancelBoFreeSwimmingCommand command) {
    return freeSwimmingRepository.findBySwimmingPoolIdAndId(command.swimmingPoolId(),
            command.freeSwimmingId())
        .orElseThrow(() -> new NoSuchElementException("자유수영이 존재하지 않습니다."));
  }

}
