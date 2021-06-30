package com.zeroxcc.backend.newsletter.repository;

import com.zeroxcc.backend.newsletter.model.ScheduleQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ScheduleDoneRepository extends JpaRepository<ScheduleQueue, UUID> {
}
