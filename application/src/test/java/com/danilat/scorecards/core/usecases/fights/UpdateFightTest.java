package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.events.FightCreated;
import com.danilat.scorecards.core.domain.fight.events.FightUpdated;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UpdateFightTest extends UseCaseUnitTest<Fight> {
  @Mock
  private PrimaryPort<Fight> primaryPort;
  @Spy
  private FightRepository fightRepository;
  @Mock
  private BoxerRepository boxerRepository;
  @Spy
  private EventBus eventBus;
  @Mock
  private Clock clock;
  private UpdateFight updateFight;

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  private static final Boxer ALI = BoxerMother.aBoxerWithId(new BoxerId("Ali"));
  private static final Boxer FOREMAN = BoxerMother.aBoxerWithId(new BoxerId("Foreman"));
  private static final Boxer LISTON = BoxerMother.aBoxerWithId(new BoxerId("Liston"));
  private static final BoxerId NON_EXISTING_BOXER = new BoxerId("irrelevant");
  private static final FightId FIGHT_ID = new FightId("irrelevant id");
  private String aPlace = "Kinsasa, Zaire";
  private Integer numberOfRounds = 6;
  private Instant anHappenedAt;
  private LocalDate aDate;

  @Before
  public void setup() {
    aDate = LocalDate.now();
    anHappenedAt = Instant.now();
    when(boxerRepository.get(ALI.id())).thenReturn(Optional.of(ALI));
    when(boxerRepository.get(FOREMAN.id())).thenReturn(Optional.of(FOREMAN));
    when(boxerRepository.get(LISTON.id())).thenReturn(Optional.of(LISTON));
    when(fightRepository.get(FIGHT_ID)).thenReturn(Optional.of(FightMother.aFightWithIdAndBoxers(FIGHT_ID, ALI.id(), FOREMAN.id())));
    when(clock.now()).thenReturn(anHappenedAt);
    updateFight = new UpdateFight(fightRepository, boxerRepository, eventBus, clock);
  }

  @Test
  public void givenTwoExistingBoxersAndADateThenIsUpdated() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), LISTON.id(), aDate, aPlace,
            numberOfRounds);

    updateFight.execute(primaryPort, parameters);

    Fight fight = getSuccessEntity();
    assertEquals(ALI.id(), fight.firstBoxer());
    assertEquals(LISTON.id(), fight.secondBoxer());
    assertEquals(aDate, fight.event().happenAt());
    assertEquals(aPlace, fight.event().place());
    assertEquals(numberOfRounds, fight.numberOfRounds());
  }

  @Test
  public void givenTwoExistingBoxersAndADateThenIsPersisted() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), FOREMAN.id(), aDate, aPlace,
            numberOfRounds);

    updateFight.execute(primaryPort, parameters);

    Fight fight = getSuccessEntity();
    verify(fightRepository, times(1)).save(fight);
  }

  @Captor
  ArgumentCaptor<FightUpdated> fightUpdatedArgumentCaptorCaptor;


  @Test
  public void givenTwoBoxersAndADateThenAnEventIsPublished() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), FOREMAN.id(), aDate, aPlace,
            numberOfRounds);

    updateFight.execute(primaryPort, parameters);

    Fight fight = getSuccessEntity();
    verify(eventBus, times(1)).publish(fightUpdatedArgumentCaptorCaptor.capture());
    FightUpdated fightUpdated = fightUpdatedArgumentCaptorCaptor.getValue();
    assertEquals(fight, fightUpdated.fight());
    assertEquals(anHappenedAt, fightUpdated.happenedAt());
    assertNotNull(fightUpdated.eventId().value());
  }

  @Test
  public void givenFirstBoxerIsNotExistingThenIsInvalid() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, NON_EXISTING_BOXER, FOREMAN.id(), aDate, aPlace,
            numberOfRounds);

    updateFight.execute(primaryPort, parameters);

    FieldErrors errors = getFieldErrors();
    assertEquals("Boxer: " + NON_EXISTING_BOXER + " not found", errors.getMessagesContentFor("firstBoxer"));
  }

  @Test
  public void givenSecondBoxerIsNotExistingThenIsInvalid() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), NON_EXISTING_BOXER, aDate, aPlace,
            numberOfRounds);

    updateFight.execute(primaryPort, parameters);

    FieldErrors errors = getFieldErrors();
    assertEquals("Boxer: " + NON_EXISTING_BOXER + " not found", errors.getMessagesContentFor("secondBoxer"));
  }

  @Test
  public void givenTwoBoxerButNotDateThenIsInvalid() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), FOREMAN.id(), null, aPlace,
            numberOfRounds);

    updateFight.execute(primaryPort, parameters);

    FieldErrors errors = getFieldErrors();
    assertTrue(errors.hasMessage("happenAt is mandatory"));
  }

  @Test
  public void givenNumberOfRoundsIsNotPresentThenIsInvalid() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), LISTON.id(), aDate, aPlace,
            null);

    updateFight.execute(primaryPort, parameters);

    FieldErrors errors = getFieldErrors();
    assertTrue(errors.hasMessage("numberOfRounds is mandatory"));
  }

  @Test
  public void givenLessThanThreeNumberOfRoundsThenIsInvalid() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), LISTON.id(), aDate, aPlace,
            2);

    updateFight.execute(primaryPort, parameters);

    FieldErrors errors = getFieldErrors();
    assertTrue(errors.hasMessage("numberOfRounds is less than three"));
  }

  @Test
  public void givenMoreThanTwelveNumberOfRoundsThenIsInvalid() {
    UpdateFight.UpdateFightParams parameters = new UpdateFight.UpdateFightParams(FIGHT_ID, ALI.id(), LISTON.id(), aDate, aPlace,
            13);

    updateFight.execute(primaryPort, parameters);

    FieldErrors errors = getFieldErrors();
    assertTrue(errors.hasMessage("numberOfRounds is more than twelve"));
  }
}
