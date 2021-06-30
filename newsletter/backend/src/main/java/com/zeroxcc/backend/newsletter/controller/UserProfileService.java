package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.UserProfileDAO;
import com.zeroxcc.backend.newsletter.model.Address;
import com.zeroxcc.backend.newsletter.model.UserAccount;
import com.zeroxcc.backend.newsletter.model.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequestMapping("/user/profile")
@RestController
@Slf4j
public class UserProfileService {
    @Autowired
    private UserProfileDAO userProfileDAO;

    @GetMapping
    public List<UserProfile> getAllUserProfile() {
        return Streamable.of(userProfileDAO.findAll()).toList();
    }

    @GetMapping("/count")
    public long getCount() {
        return userProfileDAO.count();
    }

    @RequestMapping("/{id}")
    public Optional<UserProfile> getById(@PathVariable("id") String id) {
        return userProfileDAO.findById(UUID.fromString(id));
    }

    @RequestMapping("/{id}/user/account")
    public UserAccount getUserAccount(@PathVariable("id") String id) {
        return userProfileDAO.findByIdUserAccount(UUID.fromString(id));
    }

    @GetMapping("/{id}/address")
    public Address getAddress(@PathVariable("id") String id) {
        return userProfileDAO.findByIdAddress(UUID.fromString(id));
    }
}
