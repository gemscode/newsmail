package com.zeroxcc.backend.newsletter.dao;
import com.zeroxcc.backend.newsletter.model.UserAccount;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserAccountDAO extends CrudRepository<UserAccount, UUID> {

}
