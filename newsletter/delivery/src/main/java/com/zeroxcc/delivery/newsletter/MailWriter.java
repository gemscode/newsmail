package com.zeroxcc.delivery.newsletter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.mail.DefaultMailErrorHandler;
import org.springframework.batch.item.mail.MailErrorHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@Slf4j
public class MailWriter implements ItemWriter<SimpleMailMessage>, InitializingBean {

    private MailSender mailSender;

    private MailErrorHandler mailErrorHandler = new DefaultMailErrorHandler();

    /**
     * A {@link MailSender} to be used to send messages in {@link #write(List)}.
     *
     * @param mailSender The {@link MailSender} to be used.
     */
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * The handler for failed messages. Defaults to a
     * {@link DefaultMailErrorHandler}.
     *
     * @param mailErrorHandler the mail error handler to set
     */
    public void setMailErrorHandler(MailErrorHandler mailErrorHandler) {
        this.mailErrorHandler = mailErrorHandler;
    }

    /**
     * Check mandatory properties (mailSender).
     *
     * @throws IllegalStateException if the mandatory properties are not set
     *
     * @see InitializingBean#afterPropertiesSet()
     */
    @Override
    public void afterPropertiesSet() throws IllegalStateException {
        Assert.state(mailSender != null, "A MailSender must be provided.");
    }

    /**
     * @param messages to send
     * @see ItemWriter#write(List)
     */
    @Override
    public void write(List<? extends SimpleMailMessage> messages) throws MailException {

        log.info("Received messages to send: "+messages.size());

        try {
            //messages.forEach( (message) -> mailSender.send(message));
            if (mailSender == null)
                log.info("mailSender is null");

            messages.forEach( (message) -> {
                log.info(String.valueOf(message.getSentDate()));
                log.info("subject: "+message.getSubject());
                log.info("body   : "+message.getText());
                mailSender.send(message);
            });
        } catch (MailSendException e) {

            Map<Object, Exception> failedMessages = e.getFailedMessages();

            failedMessages.forEach( (key, value) -> mailErrorHandler.handle((SimpleMailMessage) key, value) );
        }
    }

}
