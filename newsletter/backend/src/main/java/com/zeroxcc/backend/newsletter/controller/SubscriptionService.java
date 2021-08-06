package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.*;
import com.zeroxcc.backend.newsletter.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@Slf4j
@RequestMapping("/subscription")
public class SubscriptionService {
    @Autowired
    private SubscriptionDAO subscriptionDAO;

    @Autowired
    private SubscriptionTopicDAO subscriptionTopicDAO;

    @Autowired
    private EmailDAO emailDAO;

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserAccountDAO userAccountDAO;

    @GetMapping
    public List<Subscription> getAll() {
        return Streamable.of(subscriptionDAO.findAll()).toList();
    }

    @RequestMapping("/topic/{topicId}/email/{emailAddress}")
    public ResponseEntity check(@PathVariable("topicId") String topicId, @PathVariable("emailAddress") String emailAddress) {
        Optional<Subscription> subscription = Optional.ofNullable(subscriptionDAO.findSubscriptionByEmailAddressAndTopic(emailAddress, UUID.fromString(topicId)));
        if (subscription.isPresent())
            return ResponseEntity.status(HttpStatus.OK).body("exists");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("");
    }

    @PostMapping("/topic/{topicId}/email/{emailAddress}")
    @Transactional
    public ResponseEntity subscribe(@PathVariable("topicId") String topicId, @PathVariable("emailAddress") String emailAddress) {
        Optional<Subscription> checkSubscription = Optional.ofNullable(subscriptionDAO.findSubscriptionByEmailAddressAndTopic(emailAddress, UUID.fromString(topicId)));

        if (checkSubscription.isPresent()) {
            log.info(" found subscription for " + topicId + " - " + emailAddress);
            return ResponseEntity.status(HttpStatus.CONFLICT).body("subscription exists already");
        } else {
            Account account = new Account();
            account.setName("generic");
            log.info("inserting account");
            accountDAO.save(account);

            UserAccount user = new UserAccount();
            user.setAccount(account);
            user.setUserId(emailAddress);
            user.setPassword(UUID.randomUUID().toString());

            log.info("inserting user");
            userAccountDAO.save(user);

            Email email = new Email(user, emailAddress,Boolean.TRUE);

            log.info("inserting email");
            emailDAO.save(email);

            Optional<SubscriptionTopic> topic = subscriptionTopicDAO.findById(UUID.fromString(topicId));

            Subscription subscription = new Subscription();
            subscription.setEmail(email);
            subscription.setIsMonthly(Boolean.FALSE);
            subscription.setIsWeekly(Boolean.TRUE);
            subscription.setTopic(topic.get());
            subscription.setIsActive(Boolean.TRUE);

            subscriptionDAO.save(subscription);
        }

        return ResponseEntity.status(HttpStatus.OK).body("");
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
}
