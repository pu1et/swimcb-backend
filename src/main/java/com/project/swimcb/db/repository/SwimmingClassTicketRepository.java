package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.SwimmingClassTicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassTicketRepository extends JpaRepository<SwimmingClassTicketEntity, Long> {

  int deleteBySwimmingClass_Id(long swimmingClassId);
}
