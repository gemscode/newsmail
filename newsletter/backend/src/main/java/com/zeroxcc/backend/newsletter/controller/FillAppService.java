package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.dao.PublisherAccountDAO;
import com.zeroxcc.backend.newsletter.dao.PublisherAppDAO;
import com.zeroxcc.backend.newsletter.model.PublisherAccount;
import com.zeroxcc.backend.newsletter.model.PublisherApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FillAppService {
    private final PublisherAppDAO publisherAppDAO;
    private final PublisherAccountDAO publisherAccountDAO;

    public FillAppService(PublisherAppDAO publisherAppDAO, PublisherAccountDAO publisherAccountDAO) {
        this.publisherAppDAO = publisherAppDAO;
        this.publisherAccountDAO = publisherAccountDAO;
    }

    @PostConstruct
    @Transactional
    public void fillData() {
        Optional<PublisherAccount> publisherAccount = publisherAccountDAO.findById(UUID.fromString("50cbb70a-8286-486c-9647-16c38f348610"));

        if (publisherAccount.isPresent()) {
            log.info(" found publisher with account id 50cbb70a-8286-486c-9647-16c38f348613");
            log.info(" account number " + publisherAccount.get().getAccount().getId());

            PublisherApp publisherApp = new PublisherApp();
            publisherApp.setAccount(publisherAccount.get());

            publisherAppDAO.save(publisherApp);

        }

    }

}
