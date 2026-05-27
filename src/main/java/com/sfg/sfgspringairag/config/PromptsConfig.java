package com.sfg.sfgspringairag.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:templates/prompts.st")
@Data
public class PromptsConfig {

    @Value("${movie-expert-prompt}")
    private String movieExpertPrompt;

    @Value("${movie-expert-prompt-with-metadata}")
    private String movieExpertPromptWithMetaData;

}
