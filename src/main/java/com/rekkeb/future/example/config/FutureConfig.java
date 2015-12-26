package com.rekkeb.future.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class to create the required beans
 *
 * Created by rekkeb on 26/12/15.
 */
@Configuration
public class FutureConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
