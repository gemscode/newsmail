package com.zeroxcc.content.newsletter.repository;

import com.zeroxcc.content.newsletter.model.MJMLContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ContentRepository extends JpaRepository<MJMLContent, UUID> {
    @Query("SELECT c FROM MJMLContent c WHERE c.status = 'posted' ")
    List<MJMLContent> getContentPosted();
}