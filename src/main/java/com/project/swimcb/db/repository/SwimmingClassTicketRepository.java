package com.project.swimcb.db.repository;

import com.project.swimcb.db.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SwimmingClassTicketRepository extends JpaRepository<TicketEntity, Long> {

}
