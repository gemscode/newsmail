package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.*;
import com.zeroxcc.backend.newsletter.model.*;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class DataService {
    private final AccountDAO accountDAO;
    private final EmailDAO emailDAO;
    private final AddressDAO addressDAO;
    private final UserAccountDAO userAccountDAO;
    private final UserProfileDAO userProfileDAO;

    public DataService(AccountDAO accountDAO, EmailDAO emailDAO, AddressDAO addressDAO, UserAccountDAO userAccountDAO, UserProfileDAO userProfileDAO) {
        this.accountDAO = accountDAO;
        this.emailDAO = emailDAO;
        this.addressDAO = addressDAO;
        this.userAccountDAO = userAccountDAO;
        this.userProfileDAO = userProfileDAO;
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

        userProfileDAO.save(userProfile);*/


    }
}
