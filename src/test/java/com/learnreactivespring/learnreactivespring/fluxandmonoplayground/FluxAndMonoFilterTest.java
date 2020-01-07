package com.learnreactivespring.learnreactivespring.fluxandmonoplayground;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoFilterTest {

  List<String> names = Arrays.asList("Nurul", "Akbar", "Jack", "Adam");

  @Test
  public void filterTest() {
    Flux<String> stringFlux = Flux.fromIterable(names)
        .filter(s -> s.startsWith("A")) // Akbar, Adam
        .log();

    StepVerifier.create(stringFlux)
        .expectNext("Akbar", "Adam")
        .verifyComplete();
  }

  @Test
  public void filterTestLength() {
    Flux<String> stringFlux = Flux.fromIterable(names)
        .filter(s -> s.length() == 4) // Akbar, Adam
        .log();

    StepVerifier.create(stringFlux)
        .expectNext("Jack", "Adam")
        .verifyComplete();
  }
}
