package com.danilat.scorecards.core.usecases;

import static org.junit.Assert.assertEquals;

import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.InvalidFightException;
import com.danilat.scorecards.core.usecases.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test
  public void twoBoxerButNotDateIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("happenAt is mandatory");
    
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, null);

    registerFight.execute(parameters);
  }

  @Test
  public void firstBoxerIsNotPresentIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("firstBoxer is mandatory");

    RegisterFightParameters parameters = new RegisterFightParameters(null, FOREMAN, aDate);

    registerFight.execute(parameters);
  }

  @Test
  public void secondBoxerIsNotPresentIsInvalid() {
    expectedEx.expect(InvalidFightException.class);
    expectedEx.expectMessage("secondBoxer is mandatory");

    RegisterFightParameters parameters = new RegisterFightParameters(ALI, null, aDate);

    registerFight.execute(parameters);
  }
}
