package com.kodilla.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;

import java.io.File;

@Configuration
public class FileIntegrationConfiguration {
    @Bean
    IntegrationFlow fileIntegrationFlow(
            FileReadingMessageSource fileAdapter,
            FileNameTransformer transformer,
            FileWritingMessageHandler outputFileHandler) {

        return IntegrationFlows.from(fileAdapter, config -> config.poller(Pollers.fixedDelay(1000)))
                .transform(transformer, "transformFileName")
                .handle(outputFileHandler)
                .get();
    }

    @Bean
    FileReadingMessageSource fileAdapter() {
        FileReadingMessageSource fileSource = new FileReadingMessageSource();
        fileSource.setDirectory(new File("data/input"));

        return fileSource;
    }

    @Bean
    FileWritingMessageHandler outputFileAdapter() {
        File directory = new File("data/output");
        FileWritingMessageHandler handler = new FileWritingMessageHandler(directory);
        handler.setExpectReply(false);
        handler.setFileNameGenerator(message -> "fileNames.txt");
        handler.setFileExistsMode(FileExistsMode.APPEND);
        handler.setAppendNewLine(true);

        return handler;
    }

    @Bean
    FileNameTransformer transformer() {
        return new FileNameTransformer();
    }

}