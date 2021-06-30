package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.EmailDAO;
import com.zeroxcc.backend.newsletter.model.Email;
import com.zeroxcc.backend.newsletter.model.UserAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/emails")
@RestController
@Slf4j
public class EmailsService {
    @Autowired
    private EmailDAO emailDAO;
    //private EmailRepository emailRepository;

    @CrossOrigin(origins = "http://localhost:8081")
    @GetMapping
    public List<Email> getAllEmails() {
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

    @GetMapping("/test/filter")
    public Email getTestFilter() {
        log.info("REQUEST EMAIL ADDRESS SEARCH");
        return emailDAO.findEmailByAddress("email@provider.net");
    }
}
