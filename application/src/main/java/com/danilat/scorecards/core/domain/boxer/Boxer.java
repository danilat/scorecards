package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.core.domain.boxer.events.BoxerCreated;
import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;
import java.util.Objects;

public class Boxer extends Entity<BoxerId> {

  private String name;
  private String alias;
  private String boxrecUrl;

  public Boxer(BoxerId id, String name, String alias, String boxrecUrl) {
    super(id);
    this.name = name;
    this.alias = alias;
    this.boxrecUrl = boxrecUrl;
  }

  public static Boxer create(BoxerId boxerId, String name, String alias, String boxrecUrl, Instant happenedAt) {
    Boxer boxer = new Boxer(boxerId, name, alias, boxrecUrl);
    BoxerCreated boxerCreated = new BoxerCreated(happenedAt, new DomainEventId(boxerId.value() + happenedAt), boxer);
    boxer.addDomainEvent(boxerCreated);
    return boxer;
  }

  @Override
  public BoxerId id() {
    return this.id;
  }

  public String name() {
    return this.name;
  }

  public String alias() {
    return alias;
  }

  public String boxrecUrl() {
    return boxrecUrl;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Boxer boxer = (Boxer) o;
    return Objects.equals(name, boxer.name) &&
        Objects.equals(alias, boxer.alias) &&
        Objects.equals(boxrecUrl, boxer.boxrecUrl);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, alias, boxrecUrl);
  }

  @Override
  public String toString() {
    return "Boxer{" +
        "name='" + name + '\'' +
        ", alias='" + alias + '\'' +
        ", boxrecUrl='" + boxrecUrl + '\'' +
        '}';
  }
}
