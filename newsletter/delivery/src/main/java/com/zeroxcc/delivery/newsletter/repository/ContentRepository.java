package com.zeroxcc.delivery.newsletter.repository;

import com.zeroxcc.delivery.newsletter.model.MJMLContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<MJMLContent, UUID> {
    //@Query("SELECT new com.zeroxcc.delivery.newsletter.model.MJMLContent(id, content, status, createdDate, updatedDate, newsletterId) FROM MJMLContent WHERE status = 'posted' ")
    @Query("SELECT c FROM MJMLContent c WHERE c.status = 'posted' ")
    List<MJMLContent> getContentPosted();
}