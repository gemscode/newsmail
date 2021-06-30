package com.zeroxcc.backend.newsletter.dao;
import com.zeroxcc.backend.newsletter.model.Email;
import com.zeroxcc.backend.newsletter.model.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface EmailDAO extends CrudRepository<Email, UUID> {
    @Query("SELECT e FROM Email e WHERE e.address = ?1")
    Email findEmailByAddress(String address);

    @Query("SELECT ua FROM Email e JOIN e.userAccount ua WHERE e.address = ?1")
    UserAccount findUserAccountByEmailAddress(String address);
}
