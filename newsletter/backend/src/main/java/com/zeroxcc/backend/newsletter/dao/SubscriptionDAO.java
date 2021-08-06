package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.Subscription;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SubscriptionDAO extends CrudRepository<Subscription, UUID> {

    @Query("SELECT s FROM Subscription s JOIN s.email se JOIN s.topic st WHERE se.address = :address AND st.id = :uuid")
    Subscription findSubscriptionByEmailAddressAndTopic(@Param("address") String address, @Param("uuid") UUID uuid);
}
