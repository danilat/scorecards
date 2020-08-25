package com.danilat.scorecards.audit;

import com.danilat.scorecards.audit.domain.DomainEventStore;
import com.danilat.scorecards.audit.usecases.RegisterDomainEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuditInitializer {

  @Autowired
  DomainEventStore domainEventStore;

  @Bean
  public RegisterDomainEvent registerDomainEvent() {
    return new RegisterDomainEvent(domainEventStore);
  }
}

