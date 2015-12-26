package com.rekkeb.future.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Basic RestController to show the use of Futures in Java8 with Lambda functions
 *
 * Created by rekkeb on 26/12/15.
 */
@RestController
public class FutureController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FutureController.class);

    @Autowired
    private RestTemplate restTemplate;


    @RequestMapping(value = "/future", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> future(@RequestHeader HttpHeaders httpHeaders){

        //Lets propagate the received headers
        httpHeaders.add("vnd.rekkeb.random", String.valueOf(new Random().nextInt(10000)));
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        HashMap<String,String> result = new HashMap<>();

        long start = System.nanoTime();
        LOGGER.info("Starting requests...");

        Future<String> futureDelay3 = CompletableFuture.supplyAsync(() -> getDelay3(entity));
        Future<String> futureDelay5 = CompletableFuture.supplyAsync(() -> getDelay5(entity));
        Future<String> futureGet = CompletableFuture.supplyAsync(() -> get(entity));
        //This request will return a 500 Status code, so the RestTemplate will throw an exception
        Future<String> futureDelayError = CompletableFuture.supplyAsync(() -> getError500(entity));

        try {
            result.put("delay3", futureDelay3.get(10, TimeUnit.SECONDS));
            result.put("delay5", futureDelay5.get(10, TimeUnit.SECONDS));
            result.put("error500", futureDelayError.get(10, TimeUnit.SECONDS));
            result.put("get", futureGet.get(10, TimeUnit.SECONDS)); //This will never be executed due to below Exception
        }catch (Exception e){
            result.put("exception", e.getMessage());
            if (LOGGER.isDebugEnabled()){
                LOGGER.error("{}", e);
            }
        }

        long duration = (System.nanoTime() - start) / 1_000_000;
        LOGGER.info("Done in " + duration + " msecs");

        return result;

    }

    private String getDelay3(HttpEntity<?> entity){
        return restTemplate.exchange("http://httpbin.org/delay/3", HttpMethod.GET, entity, String.class).getBody();
    }
    private String getDelay5(HttpEntity<?> entity){
        return restTemplate.exchange("http://httpbin.org/delay/5", HttpMethod.GET, entity, String.class).getBody();
    }
    private String get(HttpEntity<?> entity){
        return restTemplate.exchange("http://httpbin.org/", HttpMethod.GET, entity, String.class).getBody();
    }
    private String getError500(HttpEntity<?> entity){
        return restTemplate.exchange("http://httpbin.org/status/500", HttpMethod.GET, entity, String.class).getBody();
    }

}
