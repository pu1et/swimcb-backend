package com.project.swimcb.bo.swimmingclass.application;

import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_COMPLETED;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_PENDING;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.PAYMENT_VERIFICATION;
import static com.project.swimcb.swimmingpool.domain.enums.ReservationStatus.RESERVATION_PENDING;

import com.project.swimcb.bo.swimmingclass.application.in.CancelBoSwimmingClassUseCase;
import com.project.swimcb.bo.swimmingclass.application.out.CancelBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.application.out.UpdateBoSwimmingClassDsGateway;
import com.project.swimcb.bo.swimmingclass.domain.CancelBoSwimmingClassCommand;
import com.project.swimcb.db.repository.SwimmingClassRepository;
import com.project.swimcb.swimmingpool.domain.enums.CancellationReason;
import jakarta.transaction.Transactional;
import java.util.List;
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
  private final CancelBoSwimmingClassDsGateway cancelBoSwimmingClassDsGateway;

  @Override
  public void cancelBoSwimmingClass(@NonNull CancelBoSwimmingClassCommand request) {

    val swimmingClass = swimmingClassRepository.findBySwimmingPool_IdAndId(request.swimmingPoolId(),
            request.swimmingClassId())
        .orElseThrow(
            () -> new NoSuchElementException("클래스가 존재하지 않습니다 : " + request.swimmingClassId()));

    // 입금확인중, 결제완료 있는지 검사
    if (!canBeCanceled(request.swimmingClassId())) {
      throw new IllegalStateException("입금확인중, 결제완료 건이 있어 폐강이 불가합니다 : " + request.swimmingClassId());
    }

    // 클래스 티켓 일괄 삭제
    updateBoSwimmingClassDsGateway.deleteAllTicketsBySwimmingClassId(request.swimmingClassId());

    // 예약대기, 결제대기 일괄 관리자 취소 처리
    cancelReservations(request.swimmingClassId());

    swimmingClass.cancel(request.cancelReason());
  }

  private void cancelReservations(@NonNull Long swimmingClassId) {
    // TODO. 사용자에게 취소 알림
    cancelBoSwimmingClassDsGateway.cancelAllReservationsBySwimmingClassIdAndReservationStatusIn(
        swimmingClassId,
        List.of(RESERVATION_PENDING, PAYMENT_PENDING),
        CancellationReason.SWIMMING_CLASS_CANCELLED
    );
  }

  private boolean canBeCanceled(@NonNull Long swimmingClassId) {
    return !cancelBoSwimmingClassDsGateway.existsReservationBySwimmingClassIdReservationStatusIn(
        swimmingClassId,
        List.of(PAYMENT_VERIFICATION, PAYMENT_COMPLETED)
    );
  }

}
