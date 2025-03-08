package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.application.in.CreateBoSwimmingClassSubTypeUseCase;
import com.project.swimcb.bo.swimmingclass.domain.CreateBoSwimmingClassSubTypeCommand;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubType;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassSubTypeRepository;
import com.project.swimcb.bo.swimmingclass.domain.SwimmingClassTypeRepository;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
class CreateBoSwimmingClassSubTypeInteractor implements CreateBoSwimmingClassSubTypeUseCase {

  private final SwimmingClassTypeRepository swimmingClassTypeRepository;
  private final SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Override
  public void createBoSwimmingClassSubType(@NonNull CreateBoSwimmingClassSubTypeCommand request) {
    val classType = swimmingClassTypeRepository.findById(request.swimmingClassTypeId())
        .orElseThrow(() -> new NoSuchElementException("강습형태를 찾을 수 없습니다."));

    val subType = SwimmingClassSubType.create(classType, request.name());

    swimmingClassSubTypeRepository.save(subType);
  }
}
