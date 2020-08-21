package com.danilat.scorecards.audit.usecases;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.danilat.scorecards.audit.domain.DomainEventStore;
import com.danilat.scorecards.core.shared.events.DomainEvent;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegisterDomainEventTest {

  private RegisterDomainEvent registerDomainEvent;

  @Spy
  private DomainEventStore domainEventStore;

  @Before
  public void setup() {
    registerDomainEvent = new RegisterDomainEvent(domainEventStore);
  }

  @Test
  public void givenADomainEventThenIsPersisted() {
    DomainEvent domainEvent = new DomainEvent(LocalDate.now(), "an id") {
    };

    registerDomainEvent.execute(domainEvent);

    verify(domainEventStore, times(1)).save(domainEvent);
  }
}
