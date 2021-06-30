package com.zeroxcc.delivery.newsletter.mail;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Kacem Boufelliga
 *
 * @since 2.1
 */
public class MailSender implements org.springframework.mail.MailSender {

    private List<String> subjectsToFail = new ArrayList<>();

    private List<SimpleMailMessage> received = new ArrayList<>();

    public void clear() {
        received.clear();
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        throw new UnsupportedOperationException("Not implemented.  Use send(SimpleMailMessage[]).");
    }

    public void setSubjectsToFail(List<String> subjectsToFail) {
        this.subjectsToFail = subjectsToFail;
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        Map<Object, Exception> failedMessages = new LinkedHashMap<>();
        for (SimpleMailMessage simpleMessage : simpleMessages) {
            if (subjectsToFail.contains(simpleMessage.getSubject())) {
                failedMessages.put(simpleMessage, new MessagingException());
            }
            else {
                received.add(simpleMessage);
            }
        }
        if (!failedMessages.isEmpty()) {
            throw new MailSendException(failedMessages);
        }
    }

    public List<SimpleMailMessage> getReceivedMessages() {
        return received;
    }

}