package com.zeroxcc.backend.newsletter.controller;

import com.zeroxcc.backend.newsletter.model.ScheduleQueue;
import com.zeroxcc.backend.newsletter.repository.QueueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/queue")
@RestController
@Slf4j
//@ComponentScan("com.zeroxcc.newsletter.backend.model")
public class QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @GetMapping
    public List<ScheduleQueue> getQueue() {
        //log.info(String.valueOf(queueRepository.findAll().stream().count()));
        log.info(String.valueOf((long) queueRepository.getScheduledQueue().size()));
        return queueRepository.getScheduledQueue();
    }

    @GetMapping("/update")
    public int setQueue() {
        int result = queueRepository.setScheduledQueue();

        log.info(String.valueOf(result));

        return result;
    }


}
