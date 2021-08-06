package com.zeroxcc.backend.newsletter.controller;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
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

import java.io.IOException;
import java.time.LocalDateTime;
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

    @PostMapping("/publish/{publisherAppId}/topic/{subscriptionTopicId}")
    @ResponseBody
    @Transactional
    public ResponseEntity publish(@PathVariable(name = "publisherAppId") String publisherAppId, @PathVariable(name = "subscriptionTopicId") String subscriptionTopicId, @RequestBody String content) {

        Optional<PublisherApp> publisherApp = publisherAppDAO.findById(UUID.fromString(publisherAppId));

        if (publisherApp.isPresent()) {
            PublisherAccount publisherAccount =  publisherApp.get().getAccount();

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

                return ResponseEntity.status(HttpStatus.OK).body(contentHTML.getId().toString());
            }
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("error: something went wrong!");
    }

    @PostMapping("/email/{emailAddress}/topic/{subscriptionTopicId}/content/{contentId}")
    @ResponseBody
    public ResponseEntity email(@PathVariable(name = "emailAddress") String emailAddress, @PathVariable(name = "subscriptionTopicId") String subscriptionTopicId, @PathVariable(name = "contentId") String contentId) {
        log.info("data received are: \n"+ emailAddress);
        log.info("\n"+ subscriptionTopicId);
        log.info("\n"+ contentId);
        String response = "";

        Optional<ContentHTML> contentHTML = contentHTMLDAO.findById(UUID.fromString(contentId));

        if (contentHTML.isPresent()) {
            Optional<SubscriptionTopic> subscriptionTopic = subscriptionTopicDAO.findById(UUID.fromString(subscriptionTopicId));

            if (subscriptionTopic.isPresent())
                response = sentEmail(emailAddress, subscriptionTopic.get().getName(), contentHTML.get().getContent());

            if (response.equals("OK"))
                return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(response);
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

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final String exceptionHandlerIllegalArgumentException(final IOException e) {
        return " ... invalid request ...";
    }

    private static final String FROM = "no-reply@feedback.rp78.zeroxcc.com";
    private static final BasicAWSCredentials awsCreds = new BasicAWSCredentials("x", "x");
    private static final AWSStaticCredentialsProvider awsCredsProvider = new AWSStaticCredentialsProvider(awsCreds);

    private String sentEmail(String emailAddress, String subscriptionTopic, String emailContentHTML) {
        try {
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                            .withRegion(Regions.US_EAST_2).build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination( new Destination().withToAddresses(emailAddress))
                    .withMessage(new Message()
                            .withBody(new Body() .withHtml(new Content().withCharset("UTF-8").withData(emailContentHTML)))
                            .withSubject(new Content().withCharset("UTF-8").withData(subscriptionTopic)))
                    .withSource(FROM)
                    .withRequestCredentialsProvider(awsCredsProvider);
                    // Comment or remove the next line if you are not using a
                    // configuration set
                    //.withConfigurationSetName(CONFIGSET);
            client.sendEmail(request);

            return "OK";
        } catch (Exception ex) {
            System.out.println("The email was not sent. Error message: " + ex.getMessage());
            return ex.getMessage();
        }
    }
}
