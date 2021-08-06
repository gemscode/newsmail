package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.*;
import com.zeroxcc.backend.newsletter.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class FillDataService {
    private final AccountDAO accountDAO;
    private final EmailDAO emailDAO;
    private final AddressDAO addressDAO;
    private final UserAccountDAO userAccountDAO;
    private final UserProfileDAO userProfileDAO;
    private final PublisherAccountDAO publisherAccountDAO;
    private final NewsletterDAO newsletterDAO;
    private final SubscriptionTopicDAO subscriptionTopicDAO;
    private final SubscriptionDAO subscriptionDAO;
    private final EmailGroupDAO emailGroupDAO;
    private final ScheduleTaskDAO scheduleTaskDAO;
    private final ScheduleToDoDAO scheduleToDoDAO;
    private final ScheduleDoneDAO scheduleDoneDAO;

    public FillDataService(
            AccountDAO accountDAO, EmailDAO emailDAO, AddressDAO addressDAO, UserAccountDAO userAccountDAO, 
            UserProfileDAO userProfileDAO, EmailGroupDAO emailGroupDAO,
            PublisherAccountDAO publisherAccountDAO, NewsletterDAO newsletterDAO,
            SubscriptionDAO subscriptionDAO, SubscriptionTopicDAO subscriptionTopicDAO,
            ScheduleTaskDAO scheduleTaskDAO, ScheduleToDoDAO scheduleToDoDAO, ScheduleDoneDAO scheduleDoneDAO
    
    ) {
        this.accountDAO = accountDAO;
        this.emailDAO = emailDAO;
        this.addressDAO = addressDAO;
        this.userAccountDAO = userAccountDAO;
        this.userProfileDAO = userProfileDAO;
        this.emailGroupDAO = emailGroupDAO;
        this.publisherAccountDAO = publisherAccountDAO;
        this.newsletterDAO = newsletterDAO;
        this.subscriptionDAO = subscriptionDAO;
        this.subscriptionTopicDAO = subscriptionTopicDAO;
        this.scheduleTaskDAO = scheduleTaskDAO;
        this.scheduleToDoDAO = scheduleToDoDAO;
        this.scheduleDoneDAO = scheduleDoneDAO;
    }

    @PostConstruct
    @Transactional
    public void fillData() {
        /*Account account = new Account();
        account.setName("superuser");

        accountDAO.save(account);

        UserAccount user = new UserAccount();
        user.setAccount(account);
        user.setUserId("userid@email.com");
        user.setPassword("abcshouldbeencrypted");

        userAccountDAO.save(user);

        Set<Email> emailSet = new HashSet<Email>();
        Email email = new Email(user, "email@provider.com",Boolean.TRUE);

        emailDAO.save(email);
        emailSet.add(email);

        email = new Email(user, "email@provider.net",Boolean.FALSE);

        emailDAO.save(email);
        emailSet.add(email);

        Address address = new Address();
        address.setStreetLine("Haight Street");
        address.setCity("San Francisco");
        address.setState("CA");
        address.setPostalCode("94102");
        address.setCountry("USA");

        addressDAO.save(address);

        UserProfile userProfile = new UserProfile();
        userProfile.setAccount(user);
        userProfile.setEmails(emailSet);
        userProfile.setAddress(address);

        userProfileDAO.save(userProfile);
        
        account = new Account();
        account.setName("publisher");
        
        accountDAO.save(account);
        
        PublisherAccount publisher = new PublisherAccount();
        publisher.setAccount(account);
        publisher.setUserId("publisher@email.com");
        publisher.setPassword("abcshouldbeencrypted");
        publisher.setPublisherKey("publisher key to be provided");
        
        publisherAccountDAO.save(publisher);
        
        SubscriptionTopic topic = new SubscriptionTopic();
        topic.setName("Election 2020");
        
        subscriptionTopicDAO.save(topic);
        
        Subscription subscription = new Subscription();
        subscription.setEmail(email);
        subscription.setIsMonthly(Boolean.FALSE);
        subscription.setIsWeekly(Boolean.TRUE);
        subscription.setTopic(topic);
        subscription.setIsActive(Boolean.TRUE);
        
        subscriptionDAO.save(subscription);
        
        Newsletter newsletter = new Newsletter();
        newsletter.setTopic(topic);
        newsletter.setContentUrl("https://content.redpill.com/1234567789098765432");
        newsletter.setPublisher(publisher);
        
        newsletterDAO.save(newsletter);
        
        ScheduleTask task = new ScheduleTask();
        task.setNewsletter(newsletter);
        task.setSubscriptions(subscription);
        
        scheduleTaskDAO.save(task);
        
        ScheduleToDo todo = new ScheduleToDo();
        todo.setTask(task);
        todo.setScheduledDate(LocalDateTime.now().plusDays(7));

        scheduleToDoDAO.save(todo);

        newsletter = new Newsletter();
        newsletter.setTopic(topic);
        newsletter.setContentUrl("https://content.redpill.com/1234567789098765123");
        newsletter.setPublisher(publisher);

        newsletterDAO.save(newsletter);

        task = new ScheduleTask();
        task.setNewsletter(newsletter);
        task.setSubscriptions(subscription);

        scheduleTaskDAO.save(task);

        todo = new ScheduleToDo();
        todo.setTask(task);
        todo.setScheduledDate(LocalDateTime.now());

        scheduleToDoDAO.save(todo);*/
    }
}