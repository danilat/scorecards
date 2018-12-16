package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;

public class BoxerMother {
  public static Boxer aBoxerWithId(BoxerId id){
    return aBoxerWithIdAndName(id, "a irrelevant boxer name");
  }

  public static Boxer aBoxerWithIdAndName(BoxerId id, String name) {
    return new Boxer(id, name);
  }
}
