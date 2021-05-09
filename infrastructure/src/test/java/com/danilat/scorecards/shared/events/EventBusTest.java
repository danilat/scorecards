package com.danilat.scorecards.shared.events;

import static org.junit.Assert.assertTrue;

import java.time.Instant;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SpringEventBus.class, ListenerSpy.class, SimpleMeterRegistry.class})
public class EventBusTest {

  @Autowired
  EventBus eventBus;

  @Autowired
  ListenerSpy listenerSpy;

  @Test
  public void publishAnEvent() {
    DomainEvent event = new DomainEvent(Instant.now(), "irrelevant id") {
    };
    eventBus.publish(event);
    assertTrue(listenerSpy.called);
  }
}
