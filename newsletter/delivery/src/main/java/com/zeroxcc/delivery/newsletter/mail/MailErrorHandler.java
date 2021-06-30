package com.zeroxcc.delivery.newsletter.mail;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailMessage;

import java.util.ArrayList;
import java.util.List;


public class MailErrorHandler implements org.springframework.batch.item.mail.MailErrorHandler {
    private static final Log LOGGER = LogFactory.getLog(MailErrorHandler.class);

    private List<MailMessage> failedMessages = new ArrayList<>();

    @Override
    public void handle(MailMessage failedMessage, Exception ex) {
        this.failedMessages.add(failedMessage);
        LOGGER.error("Mail message failed: " + failedMessage, ex);
    }

    public List<MailMessage> getFailedMessages() {
        return failedMessages;
    }

    public void clear() {
        this.failedMessages.clear();
    }
}
