package com.zeroxcc.backend.newsletter.repository;

import com.zeroxcc.backend.newsletter.model.SubscriptionTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public interface TopicRepository extends JpaRepository<SubscriptionTopic, UUID> {
    @Query("SELECT t FROM SubscriptionTopic t WHERE REPLACE(t.name,' ','') = REPLACE(?1, ' ', '')")
    SubscriptionTopic findSubscripitionTopicByName(String name);

    @Query("SELECT t FROM SubscriptionTopic t WHERE t.id IN (?1)")
    Set<SubscriptionTopic> findSubscriptionTopicByIds(String ids);

    @Query( "SELECT t FROM SubscriptionTopic t WHERE t.id IN :ids" )
    List<SubscriptionTopic> findBySubscriptionTopicIds(@Param("ids") List<UUID> uuidList);
}
