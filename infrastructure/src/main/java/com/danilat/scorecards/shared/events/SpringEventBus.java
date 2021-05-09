package com.danilat.scorecards.shared.events;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringEventBus implements EventBus {

  private final ApplicationEventPublisher publisher;
  private Counter.Builder eventCounter;
  private static final String EVENT_TAG = "event_name";
  private MeterRegistry registry;

  public SpringEventBus(@Autowired ApplicationEventPublisher publisher, @Autowired MeterRegistry registry) {
    this.registry = registry;
    this.eventCounter = Counter.builder("domain_events.publications").description("Domain events publications");
    this.publisher = publisher;
  }

  @Override
  public void publish(DomainEvent event) {
    this.publisher.publishEvent(event);
    Counter counter = this.eventCounter.tag(EVENT_TAG, event.type()).register(registry);
    counter.increment();
  }
}
