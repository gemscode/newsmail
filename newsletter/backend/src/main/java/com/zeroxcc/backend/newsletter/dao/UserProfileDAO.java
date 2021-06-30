package com.zeroxcc.backend.newsletter.dao;
import com.zeroxcc.backend.newsletter.model.Address;
import com.zeroxcc.backend.newsletter.model.UserAccount;
import com.zeroxcc.backend.newsletter.model.UserProfile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserProfileDAO extends CrudRepository<UserProfile, UUID> {
    @Query("SELECT ua FROM UserProfile up JOIN up.account ua WHERE up.id = ?1")
    UserAccount findByIdUserAccount(UUID id);

    @Query("SELECT a FROM UserProfile up JOIN up.address a WHERE up.id = ?1")
    Address findByIdAddress(UUID id);
}
