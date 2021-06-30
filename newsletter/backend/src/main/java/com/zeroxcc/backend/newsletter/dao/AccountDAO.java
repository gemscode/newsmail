package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AccountDAO extends CrudRepository<Account, UUID> {}
