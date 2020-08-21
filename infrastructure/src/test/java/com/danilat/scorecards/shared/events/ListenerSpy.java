package com.danilat.scorecards.shared.events;

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