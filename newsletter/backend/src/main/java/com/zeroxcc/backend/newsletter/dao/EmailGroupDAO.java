package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.EmailGroup;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailGroupDAO extends CrudRepository<EmailGroup, UUID> {
}
