package com.zeroxcc.backend.newsletter.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import com.zeroxcc.backend.newsletter.dao.*;
import com.zeroxcc.backend.newsletter.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

@CrossOrigin(origins = "http://localhost")
@RestController
@Slf4j
@RequestMapping("/emails")
public class EmailsService {
    private static final int MAX_FILE_UPLOAD_SIZE = 5000000; // in bytes equals to 5MB
    private static final Pattern emailPattern = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$"); // RFC 5322 +++
    private Set<Email> emailsSet = new HashSet<>();

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private UserAccountDAO userAccountDAO;

    @Autowired
    private EmailDAO emailDAO;

    @Autowired
    private EmailGroupDAO emailGroupDAO;

    @Autowired
    private SubscriptionTopicDAO subscriptionTopicDAO;

    @Autowired
    private SubscriptionDAO subscriptionDAO;

    @GetMapping
    public List<Email> getAll() {
        return Streamable.of(emailDAO.findAll()).toList();
    }

    @GetMapping("/count")
    public long getCount() {
        return emailDAO.count();
    }

    @RequestMapping("/filter/{address}")
    public Email getByAddress(@PathVariable("address") String address) {
        log.info("REQUEST EMAIL ADDRESS SEARCH WITH VALUE "+address);
        return emailDAO.findEmailByAddress(address);
    }

    @RequestMapping("/filter/{address}/account")
    public UserAccount getUserAccountByAddress(@PathVariable("address") String address) {
        log.info("REQUEST USER ACCOUNT BY EMAIL ADDRESS SEARCH WITH VALUE "+address);
        return emailDAO.findUserAccountByEmailAddress(address);
    }

    @PostMapping("/import")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file, @RequestParam("topics") String topics) throws IOException, CsvValidationException, JsonProcessingException {

        byte[] bytes = file.getBytes();

        if (bytes.length > MAX_FILE_UPLOAD_SIZE)
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(" ... file exceeds allowable 5MB size ... ");

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(true)
                .build();

        CSVReader reader = new CSVReaderBuilder(new InputStreamReader( new ByteArrayInputStream(bytes)))
                .withCSVParser(parser)
                .build();

        List<String> parsingErrorList = new ArrayList<>();
        List<String> insertErrorList = new ArrayList<>();

        String[] line;
        int counter  = 0;
        boolean inserted = false;
        while ((line = reader.readNext()) != null) {
            // we are going to assume that every cell is an email
            for (String email : line) {
                if (emailPattern.matcher(email).matches()) {
                   inserted = insertEmail(email, getTopicList(topics), counter);

                   if (inserted)
                       counter++;
                   else
                       insertErrorList.add(email);

                } else {
                    parsingErrorList.add(email);
                }
            }
        }

        reader.close();

        String report = " inserted: " + counter + " | rejected: " + insertErrorList.size() + " | invalid: " + parsingErrorList.size();

        return ResponseEntity.status(HttpStatus.OK).body(report);
    }

    private List<SubscriptionTopic> getTopicList(String jsonObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonObj);
        Iterator<JsonNode> list = jsonNode.iterator();

        List<UUID> uuidList = new ArrayList<>();

        while(list.hasNext()) {
            JsonNode node = list.next();
            uuidList.add(UUID.fromString(node.get("id").asText()));
        }

        return subscriptionTopicDAO.findBySubscriptionTopicIds(uuidList);
    }

    @Transactional
    private Boolean insertEmail(String emailAddress, List<SubscriptionTopic> topicList, int counter) {
        try {
            Account account = new Account();
            account.setName("generic");

            accountDAO.save(account);

            UserAccount user = new UserAccount();
            user.setAccount(account);
            user.setUserId(emailAddress);
            user.setPassword(UUID.randomUUID().toString());

            userAccountDAO.save(user);

            Email email = new Email(user, emailAddress, Boolean.TRUE);

            emailDAO.save(email);

            for (SubscriptionTopic subscriptionTopic: topicList)
            {
                Subscription subscription = new Subscription();
                subscription.setEmail(email);
                subscription.setIsMonthly(Boolean.FALSE);
                subscription.setIsWeekly(Boolean.TRUE);
                subscription.setTopic(subscriptionTopic);
                subscription.setIsActive(Boolean.TRUE);

                subscriptionDAO.save(subscription);
            }

            /*
            if (counter % 100 == 0) {
                // create email group and associate the 100 emails already processed
                // make sure to exclude the errors by having a local counter
                EmailGroup emailGroup = new EmailGroup();
                emailGroup.setName(UUID.randomUUID().toString());
                emailGroup.setEmails(emailsSet);

                emailGroupDAO.save(emailGroup);

                // empty the email set
                emailsSet = new HashSet<>();
            }
            */


        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }

        return true;
    }

    @ExceptionHandler(JsonProcessingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ResponseEntity exceptionHandlerJsonProcessingException(final JsonProcessingException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(" ... error processing topics...");
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

    /*
    @ExceptionHandler(MultipartException.class)
    public String exceptionHandlerMultipartException(MultipartException e, RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute("message", e.getCause().getMessage());
        return "redirect:/upload/status";

    }
    */

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity exceptionHandlerMultipartException(MultipartException e) {
        log.error(e.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("... error processing file ...");
    }

    @ExceptionHandler(CsvValidationException.class)
    public String exceptionHandlerMultipartException(CsvValidationException e) {
        return " ... error reading file ... ";
    }
}
