package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.SubscriptionTopic;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SubscriptionTopicDAO extends CrudRepository<SubscriptionTopic, UUID> {
    @Query("SELECT t FROM SubscriptionTopic t WHERE REPLACE(LOWER(t.name),' ','') = REPLACE(LOWER(?1), ' ', '')")
    SubscriptionTopic findSubscripitionTopicByName(String name);

    @Query( "SELECT t FROM SubscriptionTopic t WHERE t.id IN :ids" )
    List<SubscriptionTopic> findBySubscriptionTopicIds(@Param("ids") List<UUID> uuidList);
}
