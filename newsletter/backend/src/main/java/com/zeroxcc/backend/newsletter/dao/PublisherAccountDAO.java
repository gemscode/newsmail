package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.PublisherAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PublisherAccountDAO extends CrudRepository<PublisherAccount, UUID> {

}
