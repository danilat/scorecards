package com.danilat.scorecards.audit.domain;

import com.danilat.scorecards.shared.events.DomainEvent;

public interface DomainEventStore {

  void save(DomainEvent domainEvent);

  DomainEvent get(String id);
}
