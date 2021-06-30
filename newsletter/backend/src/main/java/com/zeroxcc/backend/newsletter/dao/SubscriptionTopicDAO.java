package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.SubscriptionTopic;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SubscriptionTopicDAO extends CrudRepository<SubscriptionTopic, UUID> {
}
