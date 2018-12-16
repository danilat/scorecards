package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import java.time.LocalDate;

public class FightMother {

  public static Fight aFightWithId(String id) {
    return new Fight(new FightId(id), new BoxerId("ali"), new BoxerId("foreman"), LocalDate.now());
  }
}
