package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import java.time.LocalDate;

public class FightMother {

  public static Fight aFightWithId(String id) {
    return aFightWithId(new FightId(id));
  }

  public static Fight aFightWithId(FightId id) {
    Event theRumbleInTheJungle = new Event(LocalDate.now(), "Kinsasa, Zaire");
    return new Fight(id, new BoxerId("ali"), new BoxerId("foreman"), theRumbleInTheJungle, 12);
  }

  public static Fight aFightWithIdAndNumberOfRounds(FightId id, Integer numerOfRounds) {
    Event theRumbleInTheJungle = new Event(LocalDate.now(), "Kinsasa, Zaire");
    return new Fight(id, new BoxerId("ali"), new BoxerId("foreman"), theRumbleInTheJungle, numerOfRounds);
  }
}
