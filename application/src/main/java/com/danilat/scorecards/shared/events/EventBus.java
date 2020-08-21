package com.danilat.scorecards.shared.events;

public interface EventBus {

  void publish(DomainEvent event);
}
