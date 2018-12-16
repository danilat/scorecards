package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.boxer.Boxer;

public class BoxerMother {
  public static Boxer aBoxerWithId(String id){
    return new Boxer(id, "a irrelevant boxer name");
  }
}
