package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;

public class BoxerMother {

  public static Boxer aBoxerWithId(BoxerId id) {
    return aBoxerWithIdAndName(id, "a boxer name for " + id.value());
  }

  public static Boxer aBoxerWithIdAndName(BoxerId id, String name) {
    return new Boxer(id, name);
  }

  public static Boxer aBoxerWithId(String id) {
    return aBoxerWithId(new BoxerId(id));
  }
}
