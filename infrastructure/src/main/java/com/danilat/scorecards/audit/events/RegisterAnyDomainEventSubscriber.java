package com.danilat.scorecards.audit.events;

import com.danilat.scorecards.audit.usecases.RegisterDomainEvent;
import com.danilat.scorecards.shared.events.DomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RegisterAnyDomainEventSubscriber {

  @Autowired
  private RegisterDomainEvent registerDomainEvent;

  @EventListener
  public void listen(DomainEvent domainEvent) {
    registerDomainEvent.execute(domainEvent);
  }
}
