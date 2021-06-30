package com.zeroxcc.delivery.newsletter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.zeroxcc.delivery.newsletter.utility.ThrowingConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Slf4j
public class MJMLContentWriter implements ItemWriter<AbstractMap.SimpleEntry<UUID, String>>, InitializingBean {

    @Value("${mjml.location}")
    private String mjmlPath;

    static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {

        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    Map<UUID, String> map;

    @PostConstruct
    public void init() {
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
        map = hzInstance.getMap("mjml-filenames");

        if (mjmlPath == null)
            mjmlPath = "/tmp";
    }

    @Override
    public void write(List<? extends AbstractMap.SimpleEntry<UUID, String>> list) throws IOException {

        log.info("Received messages to send: " + list.size());

        list.forEach( throwingConsumerWrapper(htmlContent -> {

            BufferedWriter writer;

            String filename = "/tmp/" + htmlContent.getKey() + ".mjml";

            writer = new BufferedWriter( new FileWriter(filename));

            writer.write(htmlContent.getValue());
            writer.close();

        }));
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
