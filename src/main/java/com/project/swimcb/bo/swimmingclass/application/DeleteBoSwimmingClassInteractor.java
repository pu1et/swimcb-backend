package com.project.swimcb.bo.swimmingclass.application;

import com.project.swimcb.bo.swimmingclass.application.in.DeleteBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.domain.DeleteBoSwimmingClassCommand;
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
public class DeleteBoSwimmingClassInteractor implements DeleteBoSwimmingClassUseCase {

  private final SwimmingClassRepository swimmingClassRepository;

  @Override
  public void deleteBoSwimmingClass(@NonNull DeleteBoSwimmingClassCommand request) {
    val count = swimmingClassRepository.deleteBySwimmingPool_IdAndId(request.swimmingPoolId(),
        request.swimmingClassId());

    if (count != 1) {
      throw new NoSuchElementException("클래스가 존재하지 않습니다.");
    }
  }
}
