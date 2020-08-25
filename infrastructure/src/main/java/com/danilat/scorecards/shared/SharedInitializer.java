package com.danilat.scorecards.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class SharedInitializer {

  @Bean
  public Clock clock() {
    return new Clock();
  }

  @Bean
  public UniqueIdGenerator uniqueIdGenerator() {
    return new UniqueIdGenerator();
  }
}
