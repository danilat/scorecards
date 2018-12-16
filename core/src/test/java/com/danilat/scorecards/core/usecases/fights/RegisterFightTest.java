package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegisterFightTest {

  private static final String ALI = "Ali";
  private static final String FOREMAN = "Foreman";
  private RegisterFight registerFight;
  private LocalDate aDate;
  @Spy
  private FightRepository fightRepository;
  @Mock
  private BoxerRepository boxerRepository;

  private static final String AN_ID = "irrelevant id";

  @Before
  public void setup() {
    aDate = LocalDate.now();
    when(fightRepository.nextId()).thenReturn(AN_ID);
    when(boxerRepository.get(ALI)).thenReturn(Optional.of(new Boxer(ALI, ALI)));
    when(boxerRepository.get(FOREMAN)).thenReturn(Optional.of(new Boxer(FOREMAN, FOREMAN)));
    registerFight = new RegisterFight(fightRepository, boxerRepository);
  }

  @Test
  public void twoBoxersAndADateTheFightIsRegistered() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate);

    Fight fight = registerFight.execute(parameters);

    assertEquals(aDate, fight.happenAt());
    assertEquals(ALI, fight.firstBoxer());
    assertEquals(FOREMAN, fight.secondBoxer());
    assertEquals(AN_ID, fight.id());
  }

  @Test
  public void twoBoxersAndADateTheFightIsPersisted() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate);

    Fight fight = registerFight.execute(parameters);

    verify(fightRepository, times(1)).save(fight);
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
