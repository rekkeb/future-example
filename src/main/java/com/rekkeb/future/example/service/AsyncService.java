package com.rekkeb.future.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

/**
 * Simple service to show the use of @Async and Futures in Java 8
 *
 * Created by rekkeb on 8/1/16.
 */
@Service
public class AsyncService {

    @Autowired private RestTemplate restTemplate;

    @Async
    public Future<String> getDelay(HttpEntity<?> entity, int delay){
        String result = restTemplate.exchange("http://httpbin.org/delay/"+delay, HttpMethod.GET, entity, String.class).getBody();

        return new AsyncResult<>(result);
    }

    @Async
    public Future<String> get(HttpEntity<?> entity){
        String result = restTemplate.exchange("http://httpbin.org/get", HttpMethod.GET, entity, String.class).getBody();

        return new AsyncResult<>(result);
    }

}
