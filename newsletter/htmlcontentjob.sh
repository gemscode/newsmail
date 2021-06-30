#!/bin/sh

java -jar content/target/content-0.0.1-SNAPSHOT.jar org.springframework.batch.core.launch.support.CommandLineJobRunner com.zeroxcc.content.configuration.BatchConfiguration HTMLContentJob
