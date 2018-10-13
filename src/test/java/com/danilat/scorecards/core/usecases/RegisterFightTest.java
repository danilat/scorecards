package com.danilat.scorecards.core.usecases;

import static org.junit.Assert.assertEquals;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.InvalidFightException;
import com.danilat.scorecards.core.usecases.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Test;

public class RegisterFightTest {

  private static final String ALI = "Ali";
  private static final String FOREMAN = "Foreman";
  private RegisterFight registerFight;
  private LocalDate aDate;

  @Before
  public void setup() {
    registerFight = new RegisterFight();
    aDate = LocalDate.now();
  }

  @Test
  public void twoBoxersAndADateTheFightIsRegistered() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate);

    Fight fight = registerFight.execute(parameters);

    assertEquals(aDate, fight.happenAt());
    assertEquals(ALI, fight.firstBoxer());
    assertEquals(FOREMAN, fight.secondBoxer());
  }

  @Test(expected = InvalidFightException.class)
  public void twoBoxerButNotDateIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, null);

    registerFight.execute(parameters);
  }

  @Test(expected = InvalidFightException.class)
  public void firstBoxerIsNotPresentIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(null, FOREMAN, aDate);

    registerFight.execute(parameters);
  }

  @Test(expected = InvalidFightException.class)
  public void secondBoxerIsNotPresentIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, null, aDate);

    registerFight.execute(parameters);
  }
}
