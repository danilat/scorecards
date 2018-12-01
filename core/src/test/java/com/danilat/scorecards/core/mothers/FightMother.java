package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.Fight;
import java.time.LocalDate;

public class FightMother {
  public static Fight aFightWithId(String id){
    return new Fight(id, "ali", "foreman", LocalDate.now());
  }
}
