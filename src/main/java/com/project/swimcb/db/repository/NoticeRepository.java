package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.NoticeEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<NoticeEntity, Long> {

  Page<NoticeEntity> findAllBySwimmingPool_Id(long swimmingPoolId, @NonNull Pageable pageable);

  Page<NoticeEntity> findAllBySwimmingPool_IdAndIsVisibleIsTrue(
      long swimmingPoolId,
      @NonNull Pageable pageable
  );

}
