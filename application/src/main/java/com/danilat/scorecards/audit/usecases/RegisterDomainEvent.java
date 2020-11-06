package com.danilat.scorecards.audit.usecases;

import com.danilat.scorecards.audit.domain.DomainEventStore;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.UseCase;

public class RegisterDomainEvent implements UseCase<DomainEvent, DomainEvent> {

  private final DomainEventStore domainEventStore;

  public RegisterDomainEvent(DomainEventStore domainEventStore) {
    this.domainEventStore = domainEventStore;
  }

  public void execute(
      DomainEvent domainEvent, PrimaryPort<DomainEvent> primaryPort) {
    domainEventStore.save(domainEvent);
    primaryPort.success(domainEvent);
  }
}
