package com.danilat.scorecards.shared.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventBus implements EventBus {

  private final ApplicationEventPublisher publisher;

  public SpringEventBus(@Autowired ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void publish(DomainEvent event) {
    this.publisher.publishEvent(event);
  }
}
