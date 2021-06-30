package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.Address;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface AddressDAO extends CrudRepository<Address, UUID> {

}
