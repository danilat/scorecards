package com.danilat.scorecards.shared.domain;

import com.danilat.scorecards.shared.events.DomainEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

  ;

  @Override
  public String toString() {
    return getClass().getName() + "{" +
        "id=" + id +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Entity<?> entity = (Entity<?>) o;
    return Objects.equals(id, entity.id) &&
        Objects.equals(domainEvents, entity.domainEvents);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, domainEvents);
  }

  public List<DomainEvent> domainEvents() {
    return domainEvents;
  }

  public void addDomainEvent(DomainEvent event) {
    domainEvents.add(event);
  }
}
