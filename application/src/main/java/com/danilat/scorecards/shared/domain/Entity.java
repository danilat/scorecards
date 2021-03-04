package com.danilat.scorecards.shared.domain;

import com.danilat.scorecards.shared.events.DomainEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class Entity<I extends Id> {

  protected I id;
  private List<DomainEvent> domainEvents;

  public Entity(I id) {
    this.id = id;
    domainEvents = new ArrayList<>();
  }

  public I id() {
    return id;
  }

  public List<DomainEvent> domainEvents() {
    return domainEvents;
  }

  public void addDomainEvent(DomainEvent event) {
    domainEvents.add(event);
  }
}
