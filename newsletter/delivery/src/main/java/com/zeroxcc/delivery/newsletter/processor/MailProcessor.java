package com.zeroxcc.delivery.newsletter.processor;

import com.zeroxcc.delivery.newsletter.model.ScheduleQueue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.Nullable;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

@Slf4j
@ComponentScan("com.zeroxcc.delivery.newsletter.model")
public class MailProcessor implements ItemProcessor<ScheduleQueue, SimpleMailMessage> {

    @Nullable
    @Override
    public SimpleMailMessage process(ScheduleQueue queue) throws Exception {

        log.info(" --- Mail processor invoked ---");

        log.info("message from the queue: "+queue.getEmail());

        SimpleMailMessage message = new SimpleMailMessage();
        //message.setTo( queue.getEmail() );
        message.setTo( "admin@mail.zeroxcc.com" );
        message.setFrom( "admin@mail.zeroxcc.com" );
        message.setSubject( "Test subscription" );
        message.setSentDate( new Date() );
        message.setText( "Hello " + queue.getContentUrl() );
        return message;
    }
}