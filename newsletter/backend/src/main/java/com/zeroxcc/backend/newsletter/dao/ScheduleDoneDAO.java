package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.ScheduleDone;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ScheduleDoneDAO extends CrudRepository<ScheduleDone, UUID> {

}
