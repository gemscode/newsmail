package com.zeroxcc.content.newsletter;

import com.hazelcast.core.HazelcastInstance;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class HTMLContentWriter implements ItemWriter<Pair<UUID,String>>, InitializingBean {


    /**
     * @param list mjml content to transform to html and write to cache
     * @see ItemWriter#write(List)
     */
    @Override
    public void write(List<? extends Pair<UUID,String>> list) throws Exception {

        log.info("Received messages to send: " + list.size());

        try {
            list.forEach( (htmlContent) -> {
                log.info("processing transformation of content with id: " + htmlContent.getKey());

                //map.put(htmlContent.getKey(),htmlContent.getValue());

            });
        } catch (Exception e) {
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }


}
