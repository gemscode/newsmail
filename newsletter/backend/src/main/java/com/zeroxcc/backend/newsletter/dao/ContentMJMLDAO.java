package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.ContentMJML;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ContentMJMLDAO extends CrudRepository<ContentMJML, UUID> {

}
