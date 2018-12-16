package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;

public class BoxerMother {
  public static Boxer aBoxerWithId(BoxerId id){
    return new Boxer(id, "a irrelevant boxer name");
  }
}
