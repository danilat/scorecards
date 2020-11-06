package com.danilat.scorecards.core.usecases.fights;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundException;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.events.FightCreated;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegisterFightTest {

  private static final BoxerId ALI = new BoxerId("Ali");
  private static final BoxerId FOREMAN = new BoxerId("Foreman");
  private static final BoxerId NON_EXISTING_BOXER = new BoxerId("NON_EXISTING_BOXER");
  private RegisterFight registerFight;
  private LocalDate aDate;
  @Spy
  private FightRepository fightRepository;
  @Mock
  private BoxerRepository boxerRepository;
  @Spy
  private EventBus eventBus;
  @Mock
  private Clock clock;
  @Mock
  private UniqueIdGenerator uniqueIdGenerator;
  @Mock
  private PrimaryPort<Fight> primaryPort;

  private static final String AN_ID = "irrelevant id";
  private String aPlace = "Kinsasa, Zaire";
  private Integer numberOfRounds = 12;
  private LocalDate anHappenedAt;

  @Before
  public void setup() {
    aDate = LocalDate.now();
    anHappenedAt = LocalDate.now();
    when(uniqueIdGenerator.next()).thenReturn(AN_ID);
    when(boxerRepository.get(ALI)).thenReturn(Optional.of(BoxerMother.aBoxerWithId(ALI)));
    when(boxerRepository.get(FOREMAN)).thenReturn(Optional.of(BoxerMother.aBoxerWithId(FOREMAN)));
    when(clock.now()).thenReturn(anHappenedAt);
    when(uniqueIdGenerator.next()).thenReturn(AN_ID);
    registerFight = new RegisterFight(fightRepository, boxerRepository, eventBus, clock,
        uniqueIdGenerator);
  }

  @Captor
  ArgumentCaptor<Fight> fightArgumentCaptor;

  private Fight getFight() {
    verify(primaryPort).success(fightArgumentCaptor.capture());
    Fight fight = fightArgumentCaptor.getValue();
    return fight;
  }

  @Captor
  ArgumentCaptor<Errors> errorsArgumentCaptor;

  private Errors getErrors() {
    verify(primaryPort).error(errorsArgumentCaptor.capture());
    return errorsArgumentCaptor.getValue();
  }

  @Test
  public void givenTwoBoxersAndADateThenIsRegistered() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Fight fight = getFight();
    assertEquals(ALI, fight.firstBoxer());
    assertEquals(FOREMAN, fight.secondBoxer());
    assertEquals(AN_ID, fight.id().value());
    assertEquals(aDate, fight.event().happenAt());
    assertEquals(aPlace, fight.event().place());
    assertEquals(numberOfRounds, fight.numberOfRounds());
  }

  @Test
  public void givenTwoBoxersAndADateThenIsPersisted() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Fight fight = getFight();
    verify(fightRepository, times(1)).save(fight);
  }

  @Captor
  ArgumentCaptor<FightCreated> fightCreatedArgumentCaptorCaptor;

  @Test
  public void givenTwoBoxersAndADateThenAnEventIsPublished() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Fight fight = getFight();
    verify(eventBus, times(1)).publish(fightCreatedArgumentCaptorCaptor.capture());
    FightCreated fightCreated = fightCreatedArgumentCaptorCaptor.getValue();
    assertEquals(fight, fightCreated.fight());
    assertEquals(AN_ID, fightCreated.eventId().value());
    assertEquals(anHappenedAt, fightCreated.happenedAt());
  }

  @Test
  public void givenTwoBoxerButNotDateThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, FOREMAN, null, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("happenAt"));
    assertTrue(errors.hasMessage("happenAt is mandatory"));
  }

  @Test
  public void givenFirstBoxerIsNotPresentThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(null, FOREMAN, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("firstBoxer"));
    assertTrue(errors.hasMessage("firstBoxer is mandatory"));
  }

  @Test
  public void givenFirstBoxerIsNotExistingThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(NON_EXISTING_BOXER, FOREMAN,
        aDate, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("firstBoxer"));
    assertTrue(errors.hasMessage(NON_EXISTING_BOXER.toString() + " not found"));
  }

  @Test
  public void givenSecondBoxerIsNotPresentThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, null, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("secondBoxer"));
    assertTrue(errors.hasMessage("secondBoxer is mandatory"));
  }

  @Test
  public void givenSecondBoxerIsNotExistingThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("secondBoxer"));
    assertTrue(errors.hasMessage(NON_EXISTING_BOXER.toString() + " not found"));
  }

  @Test
  public void givenFirstAndSecondBoxersAreTheSameThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, ALI, aDate, aPlace,
        numberOfRounds);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasMessage("firstBoxer and secondBoxer should be different"));
  }

  @Test
  public void givenNumberOfRoundsIsNotPresentThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        null);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("numberOfRounds"));
    assertTrue(errors.hasMessage("numberOfRounds is mandatory"));
  }

  @Test
  public void givenLessThanThreeNumberOfRoundsThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        2);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("numberOfRounds"));
    assertTrue(errors.hasMessage("numberOfRounds is less than three"));
  }

  @Test
  public void givenMoreThanTwelveNumberOfRoundsThenIsInvalid() {
    RegisterFightParameters parameters = new RegisterFightParameters(ALI, NON_EXISTING_BOXER, aDate,
        aPlace,
        13);

    registerFight.execute(primaryPort, parameters);

    Errors errors = getErrors();
    assertTrue(errors.hasError("numberOfRounds"));
    assertTrue(errors.hasMessage("numberOfRounds is more than twelve"));
  }
}
