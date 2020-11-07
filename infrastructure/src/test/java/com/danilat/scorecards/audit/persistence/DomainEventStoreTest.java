package com.danilat.scorecards.audit.persistence;

import static org.junit.Assert.assertEquals;

import com.danilat.scorecards.audit.domain.DomainEventStore;
import com.danilat.scorecards.shared.events.DomainEvent;
import java.time.Instant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = InMemoryDomainEventStore.class)
public class DomainEventStoreTest {

  @Autowired
  DomainEventStore domainEventStore;

  @Test
  public void storeAnEvent() {
    DomainEvent event = new DomainEvent(Instant.now(), "foobar") {
    };

    domainEventStore.save(event);

    assertEquals(event, domainEventStore.get(event.eventId()));
  }
}
