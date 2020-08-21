package com.danilat.scorecards.shared.events;

import com.danilat.scorecards.core.shared.events.DomainEvent;
import com.danilat.scorecards.core.shared.events.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

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
