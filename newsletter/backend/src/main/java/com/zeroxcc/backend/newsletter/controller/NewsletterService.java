package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.*;
import com.zeroxcc.backend.newsletter.model.*;
import com.zeroxcc.backend.newsletter.utility.NewsletterStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@Slf4j
@RequestMapping("/newsletter")
public class NewsletterService {
    @Autowired
    private PublisherAppDAO publisherAppDAO;

    @Autowired
    private SubscriptionTopicDAO subscriptionTopicDAO;

    @Autowired
    private NewsletterDAO newsletterDAO;

    @Autowired
    private ContentHTMLDAO contentHTMLDAO;


    //@RequestMapping(method = RequestMethod.GET, path = "/publisher")
    //public  List<PublisherApp> publisherApps () {
    //    return Streamable.of(publisherAppDAO.findAll()).toList();
    //}

    @PostMapping("/publish/{publisherAppId}/topic/{subscriptionTopicId}")
    @ResponseBody
    @Transactional
    public ResponseEntity publish(@PathVariable(name = "publisherAppId") String publisherAppId, @PathVariable(name = "subscriptionTopicId") String subscriptionTopicId, @RequestBody String content) {
        log.info(" received request with " + content);
        log.info(" received request with " + publisherAppId);
        log.info(" received request with " + subscriptionTopicId);
        String contentId = "";

        Optional<PublisherApp> publisherApp = publisherAppDAO.findById(UUID.fromString(publisherAppId));

        if (publisherApp.isPresent()) {
            // get account
            PublisherAccount publisherAccount =  publisherApp.get().getAccount();

            // get topic by name
            Optional<SubscriptionTopic> subscriptionTopic = subscriptionTopicDAO.findById(UUID.fromString(subscriptionTopicId));
            if (subscriptionTopic.isPresent()) {

                Newsletter newsletter = new Newsletter();
                newsletter.setTopic(subscriptionTopic.get());
                newsletter.setContentUrl("");
                newsletter.setPublisher(publisherAccount);

                newsletterDAO.save(newsletter);

                ContentHTML contentHTML = new ContentHTML();
                contentHTML.setContent(content);
                contentHTML.setNewsletter(newsletter);
                contentHTML.setCreatedDate(LocalDateTime.now());
                contentHTML.setUpdatedDate(LocalDateTime.now());
                contentHTML.setStatus(NewsletterStatus.PUBLISHED);

                contentHTMLDAO.save(contentHTML);

                contentId = contentHTML.getId().toString();
            } else {
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("error: something went wrong!");
            }
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("error: something went wrong!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(contentId);
    }

    @GetMapping("/content/{id}")
    @ResponseBody
    public String content(@PathVariable(name = "id") String id) {
        return contentHTMLDAO.findById(UUID.fromString(id)).get().getContent();
    }

    @GetMapping("/topics")
    @ResponseBody
    public List<SubscriptionTopic> content() {
        return Streamable.of(subscriptionTopicDAO.findAll()).toList();
    }

    @GetMapping("/publisher/{id}")
    @ResponseBody
    public Boolean publisher(@PathVariable(name = "id") String id) {

        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException();
        }

        UUID uuid = UUID.fromString(id);

        if (publisherAppDAO.findById(uuid).isPresent())
            return Boolean.TRUE;

        return Boolean.FALSE;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String exceptionHandlerIllegalArgumentException(final IllegalArgumentException e) {
        return " ... invalid request ...";
    }
}
