package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.application.in.CancelBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.CancelBoSwimmingClassCommand;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassRepository;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CancelBoSwimmingClassInteractor implements CancelBoSwimmingClassUseCase {

  private final SwimmingClassRepository swimmingClassRepository;
  private final UpdateBoSwimmingClassDsGateway updateBoSwimmingClassDsGateway;

  @Override
  public void cancelBoSwimmingClass(@NonNull CancelBoSwimmingClassCommand request) {

    updateBoSwimmingClassDsGateway.deleteAllTicketsBySwimmingClassId(request.swimmingClassId());

    val swimmingClass = swimmingClassRepository.findBySwimmingPool_IdAndId(request.swimmingPoolId(),
        request.swimmingClassId())
        .orElseThrow(() -> new NoSuchElementException("클래스가 존재하지 않습니다 : " + request.swimmingClassId()));

    swimmingClass.cancel(request.cancelReason());
  }
}
