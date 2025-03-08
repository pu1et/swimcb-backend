package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.application.in.UpdateBoSwimmingClassSubTypeUseCase;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.UpdateBoSwimmingClassSubTypeCommand;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
class UpdateBoSwimmingClassSubTypeInteractor implements UpdateBoSwimmingClassSubTypeUseCase {

  private final SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Override
  public void updateBoSwimmingClassSubType(@NonNull UpdateBoSwimmingClassSubTypeCommand request) {
    val classSubType = swimmingClassSubTypeRepository.findBySwimmingClassType_IdAndId(
            request.swimmingClassTypeId(), request.swimmingClassSubTypeId())
        .orElseThrow(() -> new NoSuchElementException("강습구분을 찾을 수 없습니다."));

    classSubType.updateName(request.name());
  }
}
