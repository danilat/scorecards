package com.danilat.scorecards.audit.events;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

import com.danilat.scorecards.audit.usecases.RegisterDomainEvent;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.SpringEventBus;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RegisterAnyDomainEventSubscriber.class, SpringEventBus.class})
public class RegisterAnyDomainEventsSubscriberTest {

  @Autowired
  SpringEventBus eventBus;

  @MockBean
  RegisterDomainEvent registerDomainEvent;

  @Test
  public void subscriberIsCalledForAllDomainEvents() {
    DomainEvent event = new DomainEvent(LocalDate.now(), "foobar") {
    };
    eventBus.publish(event);

    verify(registerDomainEvent, times(1)).execute(any(PrimaryPort.class), eq(event));
  }
}
