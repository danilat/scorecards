package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import java.time.LocalDate;

public class FightWithBoxersMother {

  public static FightWithBoxers aFightWithBoxersWithId(FightId id) {
    String firstBoxerName = "Ali";
    String secondBoxerName = "Foreman";
    LocalDate happenAt = LocalDate.now();
    String place = "Kinsasa, Zaire";
    Integer numberOfRounds = 12;
    return new FightWithBoxers(id, new BoxerId(firstBoxerName), firstBoxerName, new BoxerId(secondBoxerName), secondBoxerName, place, happenAt,
        numberOfRounds);
  }
}
