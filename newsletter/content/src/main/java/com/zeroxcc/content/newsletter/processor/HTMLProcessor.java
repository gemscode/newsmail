package com.zeroxcc.content.newsletter.processor;

import com.zeroxcc.content.newsletter.model.MJMLContent;
import es.atrujillo.mjml.config.template.TemplateFactory;
import javafx.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Slf4j
@ComponentScan("com.zeroxcc.content.newsletter.model")
public class HTMLProcessor implements ItemProcessor<MJMLContent, Pair<UUID,String>> {

    @Nullable
    @Override
    public Pair<UUID, String> process(MJMLContent mjml) throws Exception {

        log.info(" --- MJML processor invoked ---");

        log.info("mjml content: " + mjml.getContent());

        /*
         * Context contextVars = new Context();
         * contextVars.setVariable("message","Hello MJML");
         */

        String htmlContent = TemplateFactory.builder()
                .withStringTemplate()
                .template(mjml.getContent())
                .buildTemplate();

        return new Pair<UUID, String>(mjml.getId(), htmlContent);
    }
}