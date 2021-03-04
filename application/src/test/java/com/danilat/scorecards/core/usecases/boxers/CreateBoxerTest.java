package com.danilat.scorecards.core.usecases.boxers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.boxer.events.BoxerCreated;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer.CreateBoxerParams;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import java.time.Instant;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

public class CreateBoxerTest extends UseCaseUnitTest<Boxer> {

  @Mock
  private BoxerRepository boxerRepository;

  @Mock
  private UniqueIdGenerator uniqueIdGenerator;

  @Mock
  private PrimaryPort<Boxer> primaryPort;

  @Mock
  private Clock clock;

  @Spy
  private EventBus eventBus;
  private Instant anHappenedAt = Instant.now();

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  private static final String AN_ID = "irrelevant id";
  private String name = "Manny Pacquiao";
  private String alias = "pacman";
  private String boxrecUrl = "https://boxrec.com/en/proboxer/6129";
  private CreateBoxer createBoxer;

  @Before
  public void setup() {
    when(clock.now()).thenReturn(anHappenedAt);
    when(uniqueIdGenerator.next()).thenReturn(AN_ID);
    createBoxer = new CreateBoxer(uniqueIdGenerator, boxerRepository, eventBus, clock);
  }

  @Test
  public void givenNameAliasAndBoxrecUrlThenIsCreated() {
    CreateBoxerParams params = new CreateBoxerParams(name, alias, boxrecUrl);

    createBoxer.execute(primaryPort, params);

    Boxer boxer = getSuccessEntity();
    assertEquals(AN_ID, boxer.id().value());
    assertEquals(params.getName(), boxer.name());
    assertEquals(params.getAlias(), boxer.alias());
    assertEquals(params.getBoxrecUrl(), boxer.boxrecUrl());
  }

  @Test
  public void givenNameAliasAndBoxrecUrlThenIsPersisted() {
    CreateBoxerParams params = new CreateBoxerParams(name, alias, boxrecUrl);

    createBoxer.execute(primaryPort, params);

    Boxer boxer = getSuccessEntity();
    verify(boxerRepository, times(1)).save(boxer);
  }

  @Captor
  private ArgumentCaptor<BoxerCreated> boxerCreatedArgumentCaptor;

  @Test
  public void givenNameAliasAndBoxrecUrlThenAnEventIsPublished() {
    CreateBoxerParams params = new CreateBoxerParams(name, alias, boxrecUrl);

    createBoxer.execute(primaryPort, params);

    Boxer boxer = getSuccessEntity();
    verify(eventBus, times(1)).publish(boxerCreatedArgumentCaptor.capture());
    BoxerCreated boxerCreated = boxerCreatedArgumentCaptor.getValue();
    assertEquals(boxer, boxerCreated.boxer());
    assertEquals(anHappenedAt, boxerCreated.happenedAt());
    assertNotNull(boxerCreated.eventId());
  }

  @Test
  public void givenAliasAndBoxrecUrlButNotNameThenIsInvalid() {
    CreateBoxerParams params = new CreateBoxerParams("", alias, boxrecUrl);

    createBoxer.execute(primaryPort, params);

    FieldErrors errors = getErrors();
    assertTrue(errors.hasMessage("name is mandatory"));
  }
}
