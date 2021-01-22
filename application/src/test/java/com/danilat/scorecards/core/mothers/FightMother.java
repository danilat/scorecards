package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import java.time.LocalDate;

public class FightMother extends BaseMother {

  public static Fight aFightWithId(String id) {
    return aFightWithId(new FightId(id));
  }

  public static Fight aFightWithId(FightId id) {
    Event theRumbleInTheJungle = new Event(LocalDate.now(), faker().rickAndMorty().location());
    return new Fight(id, new BoxerId("ali"), new BoxerId("foreman"), theRumbleInTheJungle, 12);
  }

  public static Fight aFightWithIdAndNumberOfRounds(FightId id, Integer numberOfRounds) {
    Event theRumbleInTheJungle = new Event(LocalDate.now(), faker().rickAndMorty().location());
    return new Fight(id, new BoxerId("ali"), new BoxerId("foreman"), theRumbleInTheJungle, numberOfRounds);
  }

  public static Fight aFightWithIdAndBoxers(FightId id, BoxerId firstBoxerId, BoxerId secondBoxerId) {
    Event someEvent = new Event(LocalDate.now(), faker().rickAndMorty().location());
    return new Fight(id, firstBoxerId, secondBoxerId, someEvent, 12);
  }
}
