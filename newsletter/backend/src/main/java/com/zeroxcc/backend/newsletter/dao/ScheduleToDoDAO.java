package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.ScheduleToDo;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ScheduleToDoDAO extends CrudRepository<ScheduleToDo, UUID> {
}
