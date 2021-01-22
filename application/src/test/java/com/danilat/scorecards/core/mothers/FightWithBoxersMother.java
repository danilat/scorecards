package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import java.time.LocalDate;

public class FightWithBoxersMother extends BaseMother {

  public static FightWithBoxers aFightWithBoxersWithId(FightId id) {
    String firstBoxerName = faker().rickAndMorty().character();
    String secondBoxerName = faker().rickAndMorty().character();
    LocalDate happenAt = LocalDate.now();
    String place = faker().rickAndMorty().location();
    Integer numberOfRounds = 12;
    return new FightWithBoxers(id, new BoxerId(firstBoxerName), firstBoxerName, new BoxerId(secondBoxerName), secondBoxerName, place, happenAt,
        numberOfRounds);
  }
}
