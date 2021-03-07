package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.core.domain.boxer.events.BoxerCreated;
import com.danilat.scorecards.core.domain.boxer.events.BoxerUpdated;
import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
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

  public void change(String name, String alias, String boxrecUrl, Instant happenedAt) {
    this.name = name;
    this.alias = alias;
    this.boxrecUrl = boxrecUrl;
    BoxerUpdated boxerUpdated = new BoxerUpdated(happenedAt, new DomainEventId(this.id().value() + happenedAt), this);
    this.addDomainEvent(boxerUpdated);
  }
}
