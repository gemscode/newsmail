package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.Newsletter;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface NewsletterDAO extends CrudRepository<Newsletter, UUID> {

}