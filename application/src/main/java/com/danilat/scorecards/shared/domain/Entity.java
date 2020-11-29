package com.danilat.scorecards.shared.domain;

import com.danilat.scorecards.shared.events.DomainEvent;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity<I extends Id> {

  protected I id;
  private List<DomainEvent> domainEvents;

  public Entity(I id) {
    this.id = id;
    domainEvents = new ArrayList<>();
  }

  public I id(){
    return id;
  };

  @Override
  public String toString() {
    return getClass().getName() + "{" +
        "id=" + id +
        '}';
  }

  public List<DomainEvent> domainEvents() {
    return domainEvents;
  }

  public void addDomainEvent(DomainEvent event){
    domainEvents.add(event);
  }
}
