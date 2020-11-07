package com.danilat.scorecards.audit.usecases;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.danilat.scorecards.audit.domain.DomainEventStore;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.events.DomainEvent;
import java.time.Instant;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegisterDomainEventTest extends UseCaseUnitTest<DomainEvent> {

  private RegisterDomainEvent registerDomainEvent;

  @Spy
  private DomainEventStore domainEventStore;

  @Mock
  private PrimaryPort<DomainEvent> primaryPort;

  @Before
  public void setup() {
    registerDomainEvent = new RegisterDomainEvent(domainEventStore);
  }

  @Test
  public void givenADomainEventThenIsPersisted() {
    DomainEvent domainEvent = new DomainEvent(Instant.now(), "an id") {
    };

    registerDomainEvent.execute(primaryPort, domainEvent);

    domainEvent = getSuccessEntity();
    verify(domainEventStore, times(1)).save(domainEvent);
  }

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }
}
