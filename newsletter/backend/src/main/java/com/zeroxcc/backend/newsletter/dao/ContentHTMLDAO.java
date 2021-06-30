package com.zeroxcc.backend.newsletter.dao;

import com.zeroxcc.backend.newsletter.model.ContentHTML;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ContentHTMLDAO extends CrudRepository<ContentHTML, UUID> {

}