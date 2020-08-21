package com.danilat.scorecards.shared.events;

import com.danilat.scorecards.core.shared.events.DomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class ListenerSpy {

  boolean called;

  @EventListener
  public void on(DomainEvent event) {
    called = true;
  }
}