package com.zeroxcc.delivery.newsletter.processor;

import com.zeroxcc.delivery.newsletter.model.MJMLContent;
import es.atrujillo.mjml.config.template.TemplateFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.Nullable;

import java.util.AbstractMap;
import java.util.UUID;

@Slf4j
@ComponentScan("com.zeroxcc.processor.newsletter.model")
public class HTMLProcessor implements ItemProcessor<MJMLContent, AbstractMap.SimpleEntry<UUID, String>> {

    @Nullable
    @Override
    public AbstractMap.SimpleEntry<UUID, String> process(MJMLContent mjml) throws Exception {

        log.info(" --- HTML processor invoked ---");

        String htmlContent = TemplateFactory.builder()
                .withStringTemplate()
                .template(mjml.getContent())
                .buildTemplate();

        log.info("mjml content: " + htmlContent);

        return new AbstractMap.SimpleEntry<>(mjml.getId(), htmlContent);
    }
}