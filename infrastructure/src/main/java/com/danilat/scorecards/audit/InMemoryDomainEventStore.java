package com.danilat.scorecards.audit;

import com.danilat.scorecards.audit.domain.DomainEventStore;
import com.danilat.scorecards.shared.events.DomainEvent;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryDomainEventStore implements DomainEventStore {

  protected final Map<String, DomainEvent> persistence;

  public InMemoryDomainEventStore() {
    this.persistence = new HashMap<>();
  }

  @Override
  public void save(DomainEvent domainEvent) {
    persistence.put(domainEvent.eventId(), domainEvent);
  }

  @Override
  public DomainEvent get(String id) {
    return persistence.get(id);
  }
}
