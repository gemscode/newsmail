package com.zeroxcc.delivery.newsletter;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class HTMLContentWriter implements ItemWriter<AbstractMap.SimpleEntry<UUID, String>>, InitializingBean {


    Map<UUID, String> map ;

    @PostConstruct
    public void init() {
        HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance();
        map = hzInstance.getMap("mjml-filenames");
    }

    @Override
    public void write(List<? extends AbstractMap.SimpleEntry<UUID, String>> list) {

        AtomicReference<ProcessBuilder> processBuilder = new AtomicReference<>(new ProcessBuilder());

        log.info("-- HTML Writer invoked with size: " + list.size());

            list.forEach( (htmlContent) -> {

                //String filename = map.get(htmlContent.getKey());
                String filename = "/tmp/" + htmlContent.getKey() + ".mjml";

                log.info("... about to prrocess filename: " + filename);

                processBuilder.get().command("/Users/kacemboufelliga/development/sandbox/mjml/node_modules/mjml/bin/mjml"
                , "/tmp/569dcab2-39d1-4428-a3e5-8adeb34f154b.mjml"
                , "-s"
                , "--config.validationLevel='skip'");


                String line = null;
                try {
                    Process process = processBuilder.get().start();
                    StringBuilder output = new StringBuilder();
                    BufferedReader reader = new BufferedReader( new InputStreamReader(process.getInputStream()));

                    boolean firstline = true;
                    while ((line = reader.readLine()) != null) {
                        if (firstline)
                            firstline = false;
                        else
                            output.append(line + "\n");

                    }

                    int exitVal = process.waitFor();
                    if (exitVal == 0) {
                        log.info("Success!");
                        log.info(String.valueOf(output));
                        System.exit(0);
                    } else {
                        log.info("... something went wrong! ");
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                processBuilder.set(new ProcessBuilder());

            });
    }


    @Override
    public void afterPropertiesSet() throws Exception {

    }


}
