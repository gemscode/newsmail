package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.SubscriptionTopicDAO;
import com.zeroxcc.backend.newsletter.model.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/topics")
public class TopicService {
    @Autowired
    private SubscriptionTopicDAO subscriptionTopicDAO;

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping
    public List<SubscriptionTopic> getAll() {
        return Streamable.of(subscriptionTopicDAO.findAll()).toList();
    }

    @RequestMapping("/check/{topicName}")
    public ResponseEntity checkByName(@PathVariable("topicName") String topicName) {
        SubscriptionTopic subscriptionTopic = subscriptionTopicDAO.findSubscripitionTopicByName(topicName);

        if (subscriptionTopic != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(" duplicate value");

        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @PostMapping("/{topicName}")
    public ResponseEntity create(@PathVariable("topicName") String topicName) {
        SubscriptionTopic subscriptionTopic = subscriptionTopicDAO.findSubscripitionTopicByName(topicName);

       if (subscriptionTopic == null) {

           SubscriptionTopic topic = new SubscriptionTopic();
           topic.setName(topicName);

           subscriptionTopicDAO.save(topic);

          return ResponseEntity.status(HttpStatus.OK).body("");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body("exists already");
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity delete(@PathVariable("topicId") String topicId) {
        Optional<SubscriptionTopic> subscriptionTopic = subscriptionTopicDAO.findById(UUID.fromString(topicId));

        if (subscriptionTopic.isPresent()) {

            subscriptionTopicDAO.delete(subscriptionTopic.get());

            return ResponseEntity.status(HttpStatus.OK).body("");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(" topic does not exist");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String exceptionHandlerIllegalArgumentException(final IllegalArgumentException e) {
        return " ... invalid request ...";
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String exceptionHandlerIllegalArgumentException(final IOException e) {
        return " ... invalid request ...";
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.FAILED_DEPENDENCY)
    public final String exceptionHandlerConstraintViolationException(final ConstraintViolationException e) {
        return " dependency violation ...";
    }
}