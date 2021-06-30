package com.zeroxcc.content.newsletter.dao;

import com.zeroxcc.content.newsletter.model.MJMLContent;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MJMLContentDAO extends CrudRepository<MJMLContent, UUID> {

}