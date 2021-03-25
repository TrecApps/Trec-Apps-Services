package com.trecapps.falsehoodauth.controllers;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ClientProvider
{

    @Bean
    RestTemplate getRestTemplate()
    {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        builder.rootUri("http://localhost:8082/auth");
        builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        return builder.build();
    }
}
