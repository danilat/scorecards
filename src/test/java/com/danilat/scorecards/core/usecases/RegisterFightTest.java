package com.danilat.scorecards.core.usecases;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.usecases.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import org.junit.Test;

public class RegisterFightTest {

  private static final String ALI = "Ali";
  private static final String FOREMAN = "Foreman";

  @Test
  public void givenTwoBoxersAndADateTheFightIsRegistered() {
    RegisterFight registerFight = new RegisterFight();
    LocalDate aDate = LocalDate.now();
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate);

    Fight fight = registerFight.execute(parameters);

    assertEquals(aDate, fight.happenAt());
    assertEquals(ALI, fight.firstBoxer());
    assertEquals(FOREMAN, fight.secondBoxer());
  }
}
