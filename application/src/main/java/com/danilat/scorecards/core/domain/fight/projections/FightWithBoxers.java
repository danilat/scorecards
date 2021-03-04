package com.danilat.scorecards.core.domain.fight.projections;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.Entity;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class FightWithBoxers extends Entity<FightId> {

  private final BoxerId firstBoxerId;
  private final String firstBoxerName;
  private final BoxerId secondBoxerId;
  private final String secondBoxerName;
  private final String place;
  private final LocalDate happenAt;
  private final Integer numberOfRounds;

  public FightWithBoxers(FightId id, BoxerId firstBoxerId,
      String firstBoxerName, BoxerId secondBoxerId, String secondBoxerName,
      String place,
      LocalDate happenAt, Integer numberOfRounds) {
    super(id);
    this.firstBoxerId = firstBoxerId;
    this.firstBoxerName = firstBoxerName;
    this.secondBoxerId = secondBoxerId;
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

  public BoxerId getFirstBoxerId() {
    return firstBoxerId;
  }

  public String getFirstBoxerName() {
    return firstBoxerName;
  }

  public BoxerId getSecondBoxerId() {
    return secondBoxerId;
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
