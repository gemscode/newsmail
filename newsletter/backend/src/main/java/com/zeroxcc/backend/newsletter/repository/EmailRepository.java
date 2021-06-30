package com.zeroxcc.backend.newsletter.repository;

import com.zeroxcc.backend.newsletter.model.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {
    @Query("SELECT e FROM Email e WHERE e.address = ?1")
    Email findEmailByAddress(String address);
}