package com.danilat.scorecards.audit.events;

import com.danilat.scorecards.audit.usecases.RegisterDomainEvent;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.events.DomainEvent;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RegisterAnyDomainEventSubscriber {

  @Autowired
  private RegisterDomainEvent registerDomainEvent;
  private PrimaryPort<DomainEvent> primaryPort = domainEvent -> {
    Logger log = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    log.info("Registering domain event: " + domainEvent);
  };

  @EventListener
  public void listen(DomainEvent domainEvent) {
    registerDomainEvent.execute(primaryPort, domainEvent);
  }
}
