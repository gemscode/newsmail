package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.ScheduleTask;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ScheduleTaskDAO extends CrudRepository<ScheduleTask, UUID> {
}