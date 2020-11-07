package com.danilat.scorecards.shared.events;

import java.util.List;

public interface EventBus {

  void publish(DomainEvent event);

  default void publish(List<DomainEvent> events){
    events.forEach(event -> publish(event));
  }
}
