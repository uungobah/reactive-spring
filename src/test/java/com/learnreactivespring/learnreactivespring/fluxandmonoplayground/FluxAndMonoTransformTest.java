package com.learnreactivespring.learnreactivespring.fluxandmonoplayground;

import static reactor.core.scheduler.Schedulers.parallel;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoTransformTest {

  List<String> names = Arrays.asList("Nurul", "Akbar", "Jack", "Adam");

  @Test
  public void transformUsingMap() {
    Flux<String> stringFlux = Flux.fromIterable(names)
        .map(s -> s.toUpperCase())
        .log();

    StepVerifier.create(stringFlux)
        .expectNext("NURUL", "AKBAR", "JACK", "ADAM")
        .verifyComplete();

  }

  @Test
  public void transformUsingMap_Length() {
    Flux<Integer> stringFlux = Flux.fromIterable(names)
        .map(s -> s.length())
        .log();

    StepVerifier.create(stringFlux)
        .expectNext(5, 5, 4, 4)
        .verifyComplete();

  }

  @Test
  public void transformUsingMap_Length_repeat() {
    Flux<Integer> integerFlux = Flux.fromIterable(names)
        .map(s -> s.length())
        .repeat(1)
        .log();

    StepVerifier.create(integerFlux)
        .expectNext(5, 5, 4, 4, 5, 5, 4, 4)
        .verifyComplete();

  }

  @Test
  public void transformUsingMap_Length_Filter() {
    Flux<String> stringFlux = Flux.fromIterable(names)
        .filter(s -> s.length() > 4)
        .map(s -> s.toUpperCase())
        .log();

    StepVerifier.create(stringFlux)
        .expectNext("NURUL","AKBAR")
        .verifyComplete();

  }

  @Test
  public void transformUsingFlatMap(){
    Flux<String> names =  Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")) //
        .flatMap(s -> {
          return Flux.fromIterable(convertToList(s)); // A -> List [A, newValue] , B -> List [B, newValue]
        }) // db or external service call that returns a flux -> s -> Flux<String>
        .log();

    StepVerifier.create(names)
        .expectNextCount(12)
        .verifyComplete();

  }

  private List<String> convertToList(String s) {
    try {
      Thread.sleep(1000);

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return Arrays.asList(s, "newValue");
  }

  @Test
  public void transformUsingFlatMap_usingParallel(){
    Flux<String> names =  Flux.fromIterable(Arrays.asList("A","B","C","D","E","F")) // FLux<String>
        .window(2) // Flux<Flux<String>> -> (A,B), (C,D), (E,F)
        .flatMap((s) ->
          s.map(this::convertToList).subscribeOn(parallel()) // Flux<List<String>>
            .flatMap(strings -> Flux.fromIterable(strings)) // Flux<String>
        )
        .log();

    StepVerifier.create(names)
        .expectNextCount(12)
        .verifyComplete();

  }
}
