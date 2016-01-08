package com.rekkeb.future.example.controller;

import com.rekkeb.future.example.service.AsyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
public class FutureAsyncController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FutureAsyncController.class);

    @Autowired
    private AsyncService asyncService;


    @RequestMapping(value = "/future/async", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> futureAsync(@RequestHeader HttpHeaders httpHeaders){

        //Lets propagate the received headers
        httpHeaders.add("vnd.rekkeb.random", String.valueOf(new Random().nextInt(10000)));
        HttpEntity<?> entity = new HttpEntity<>(httpHeaders);

        HashMap<String,String> result = new HashMap<>();

        long start = System.nanoTime();
        LOGGER.info("Starting async requests...");

        Future<String> futureDelay3 = asyncService.getDelay(entity, 3);
        Future<String> futureDelay5 = asyncService.getDelay(entity, 5);
        Future<String> futureGet = asyncService.get(entity);

        try {
            result.put("delay3", futureDelay3.get(10, TimeUnit.SECONDS));
            result.put("delay5", futureDelay5.get(10, TimeUnit.SECONDS));
            result.put("get", futureGet.get(10, TimeUnit.SECONDS));
        }catch (Exception e){
            result.put("exception", e.getMessage());
            if (LOGGER.isDebugEnabled()){
                LOGGER.error("{}", e);
            }
        }

        long duration = (System.nanoTime() - start) / 1_000_000;
        LOGGER.info("Done in {} msecs", duration);

        return result;

    }

}
