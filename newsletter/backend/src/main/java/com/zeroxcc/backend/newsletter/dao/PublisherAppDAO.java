package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.PublisherApp;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PublisherAppDAO extends CrudRepository<PublisherApp, UUID> {

}
