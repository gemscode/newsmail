#!/bin/sh

java -jar delivery/target/delivery-0.0.1-SNAPSHOT.jar org.springframework.batch.core.launch.support.CommandLineJobRunner com.zeroxcc.delivery.newsletter.configuration.BatchConfiguration NewsletterJob

