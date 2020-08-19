package com.danilat.scorecards.core.domain.fight.projections;

import com.danilat.scorecards.core.domain.Entity;
import com.danilat.scorecards.core.domain.fight.FightId;
import java.time.LocalDate;

public class FightWithBoxers extends Entity<FightId> {

  private final String firstBoxerName;
  private final String secondBoxerName;
  private final String place;
  private final LocalDate happenAt;
  private final Integer numberOfRounds;

  public FightWithBoxers(FightId id, String firstBoxerName, String secondBoxerName, String place,
      LocalDate happenAt, Integer numberOfRounds) {
    super(id);
    this.firstBoxerName = firstBoxerName;
    this.secondBoxerName = secondBoxerName;
    this.place = place;
    this.happenAt = happenAt;
    this.numberOfRounds = numberOfRounds;
  }

  public FightId id() {
    return id;
  }

  public String getId() {
    return id.value();
  }

  public String getFirstBoxerName() {
    return firstBoxerName;
  }

  public String getSecondBoxerName() {
    return secondBoxerName;
  }

  public String getPlace() {
    return place;
  }

  public LocalDate getHappenAt() {
    return happenAt;
  }

  public Integer getNumberOfRounds() {
    return numberOfRounds;
  }
}
