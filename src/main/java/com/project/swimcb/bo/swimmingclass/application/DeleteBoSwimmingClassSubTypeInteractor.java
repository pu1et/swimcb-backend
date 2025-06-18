package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.application.in.DeleteBoSwimmingClassSubTypeUseCase;
import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassSubTypeCommand;
import com.project.swimcb.db.repository.SwimmingClassSubTypeRepository;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteBoSwimmingClassSubTypeInteractor implements DeleteBoSwimmingClassSubTypeUseCase {

  private final SwimmingClassSubTypeRepository swimmingClassSubTypeRepository;

  @Override
  public void deleteBoSwimmingClassSubType(@NonNull DeleteBoSwimmingClassSubTypeCommand request) {
    val count = swimmingClassSubTypeRepository.deleteBySwimmingClassType_IdAndId(
        request.swimmingClassTypeId(), request.swimmingClassSubTypeId());

    if (count != 1) {
      throw new NoSuchElementException("강습구분을 찾을 수 없습니다.");
    }
  }
}
