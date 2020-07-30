package com.danilat.scorecards.core.shared.events;

public interface EventBus {

  void publish(DomainEvent event);
}
