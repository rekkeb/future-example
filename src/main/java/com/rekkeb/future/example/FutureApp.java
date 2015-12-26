package com.rekkeb.future.example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Main Spring Boot Application Class
 * Created by rekkeb on 26/12/15.
 */
@SpringBootApplication
public class FutureApp {

    public static void main(String[] args) {
        new SpringApplicationBuilder(FutureApp.class).web(true).run(args);
    }

}
