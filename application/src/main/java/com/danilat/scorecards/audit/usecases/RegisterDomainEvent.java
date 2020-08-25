package com.danilat.scorecards.audit.usecases;

import com.danilat.scorecards.audit.domain.DomainEventStore;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.UseCase;

public class RegisterDomainEvent implements UseCase<DomainEvent> {

  private final DomainEventStore domainEventStore;

  public RegisterDomainEvent(DomainEventStore domainEventStore) {
    this.domainEventStore = domainEventStore;
  }

  @Override
  public DomainEvent execute(DomainEvent domainEvent) {
    domainEventStore.save(domainEvent);
    return domainEvent;
  }
}
