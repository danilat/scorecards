package com.danilat.scorecards.audit.domain;

import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.DomainEventId;

public interface DomainEventStore {

  void save(DomainEvent domainEvent);

  DomainEvent get(DomainEventId id);
}
