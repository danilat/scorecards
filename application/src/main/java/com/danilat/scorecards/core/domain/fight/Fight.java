package com.danilat.scorecards.core.domain.fight;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.events.FightCreated;
import com.danilat.scorecards.shared.domain.Entity;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Fight extends Entity<FightId> {

  public static final int MIN_NUMBER_OF_ROUNDS_IN_A_FIGHT = 1;
  private final BoxerId firstBoxer;
  private final BoxerId secondBoxer;
  private Event event;
  private Integer numberOfRounds;

  public Fight(FightId id, BoxerId firstBoxer, BoxerId secondBoxer, Event event, Integer numberOfRounds) {
    super(id);
    this.firstBoxer = firstBoxer;
    this.secondBoxer = secondBoxer;
    this.event = event;
    this.numberOfRounds = numberOfRounds;
  }

  public BoxerId firstBoxer() {
    return this.firstBoxer;
  }

  public BoxerId secondBoxer() {
    return this.secondBoxer;
  }

  public FightId id() {
    return id;
  }

  public Event event() {
    return this.event;
  }

  public Integer numberOfRounds() {
    return this.numberOfRounds;
  }

  public boolean isTheFirstBoxer(BoxerId boxerId) {
    return firstBoxer().equals(boxerId);
  }

  public boolean isTheSecondBoxer(BoxerId boxerId) {
    return secondBoxer().equals(boxerId);
  }

  public boolean isRoundInInterval(int round) {
    return round <= numberOfRounds() && round >= MIN_NUMBER_OF_ROUNDS_IN_A_FIGHT;
  }

  public static Fight create(FightId id, BoxerId firstBoxer, BoxerId secondBoxer, Event event, Integer numberOfRounds,
      Instant happenedAt) {
    Fight fight = new Fight(id, firstBoxer, secondBoxer, event, numberOfRounds);
    fight.addDomainEvent(FightCreated.create(fight, happenedAt));
    return fight;
  }
}
