package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;

public class BoxerMother extends BaseMother {

  public static Boxer aBoxerWithId(BoxerId id) {
    return aBoxerWithIdAndName(id, faker().rickAndMorty().character());
  }

  public static Boxer aBoxerWithIdAndName(BoxerId id, String name) {
    return new Boxer(id, name, "irrelevant alias", "irrelevant url");
  }

  public static Boxer aBoxerWithId(String id) {
    return aBoxerWithId(new BoxerId(id));
  }
}
