package com.danilat.scorecards.audit.events;

import com.danilat.scorecards.audit.usecases.RegisterDomainEvent;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.events.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RegisterAnyDomainEventSubscriber {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private RegisterDomainEvent registerDomainEvent;
  private PrimaryPort<DomainEvent> primaryPort = domainEvent -> {
    logger.info("Registering domain event: {}", domainEvent.toString());
  };

  @EventListener
  public void listen(DomainEvent domainEvent) {
    registerDomainEvent.execute(primaryPort, domainEvent);
  }
}
