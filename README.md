# future-example

This project shows a simple example using Futures in Java 8 and Spring Boot.

The code shows how to make multiple requests to a REST service, how to wait the answer of each request and merge all the responses together. Also demonstrates how to make these calls in a non blocking way.

In [FutureController.java](https://github.com/rekkeb/future-example/blob/master/src/main/java/com/rekkeb/future/example/controller/FutureController.java):

```java
Future<String> futureDelay3 = CompletableFuture.supplyAsync(() -> getDelay3(entity));
Future<String> futureDelay5 = CompletableFuture.supplyAsync(() -> getDelay5(entity));

try {
    result.put("delay3", futureDelay3.get(10, TimeUnit.SECONDS));
    result.put("delay5", futureDelay5.get(10, TimeUnit.SECONDS));
}catch (Exception e){
    ...
}
```

The **getDelay5** function, takes at least 5 seconds to complete, so the whole code will take at least 5 seconds to complete and process the requests.
