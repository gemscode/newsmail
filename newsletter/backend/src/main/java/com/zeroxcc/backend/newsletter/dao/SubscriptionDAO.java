package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.Subscription;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SubscriptionDAO extends CrudRepository<Subscription, UUID> {
}
