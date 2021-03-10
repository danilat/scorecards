package com.danilat.scorecards.core.usecases.boxers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.boxer.events.BoxerUpdated;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.core.usecases.boxers.UpdateBoxer.UpdateBoxerParams;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import java.time.Instant;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;

public class UpdateBoxerTest extends UseCaseUnitTest<Boxer> {

  @Mock
  private PrimaryPort primaryPort;
  @Mock
  private BoxerRepository boxerRepository;
  @Spy
  private EventBus eventBus;
  @Mock
  private Clock clock;

  private BoxerId boxerId = new BoxerId("an-id");
  private String changedName = "changed name";
  private String changedAlias = "changed alias";
  private String changedBoxrecUrl = "http://changed";
  private Instant happenedAt = Instant.now();

  private UpdateBoxer updateBoxer;

  @Before
  public void setup(){
    Boxer existingBoxer = BoxerMother.aBoxerWithId(boxerId);
    when(boxerRepository.get(boxerId)).thenReturn(Optional.of(existingBoxer));
    when(clock.now()).thenReturn(happenedAt);
    updateBoxer = new UpdateBoxer(boxerRepository, eventBus, clock);
  }

  @Test
  public void givenAnExistingBoxerAndValidParamsThenIsUpdated() {
    UpdateBoxerParams params = new UpdateBoxerParams(boxerId, changedName, changedAlias, changedBoxrecUrl);

    updateBoxer.execute(primaryPort, params);

    Boxer boxer = getSuccessEntity();
    assertEquals(changedName, boxer.name());
    assertEquals(changedAlias, boxer.alias());
    assertEquals(changedBoxrecUrl, boxer.boxrecUrl());
  }

  @Test
  public void givenAnExistingBoxerAndValidParamsThenIsPersisted() {
    UpdateBoxerParams params = new UpdateBoxerParams(boxerId, changedName, changedAlias, changedBoxrecUrl);

    updateBoxer.execute(primaryPort, params);

    Boxer boxer = getSuccessEntity();
    verify(boxerRepository, times(1)).save(boxer);
  }

  @Captor
  private ArgumentCaptor<BoxerUpdated> boxerUpdatedArgumentCaptor;

  @Test
  public void givenAnExistingBoxerAndValidParamsThenAnEventIsPublished() {
    UpdateBoxerParams params = new UpdateBoxerParams(boxerId, changedName, changedAlias, changedBoxrecUrl);

    updateBoxer.execute(primaryPort, params);

    Boxer boxer = getSuccessEntity();
    verify(eventBus, times(1)).publish(boxerUpdatedArgumentCaptor.capture());
    BoxerUpdated boxerUpdated = boxerUpdatedArgumentCaptor.getValue();
    assertEquals(boxer, boxerUpdated.boxer());
    assertEquals(happenedAt, boxerUpdated.happenedAt());
    assertNotNull(boxerUpdated.eventId());
  }

  @Test
  public void givenAnExistingBoxerButNoNameInParamsThenIsInvalid() {
    UpdateBoxerParams params = new UpdateBoxerParams(boxerId, "", changedAlias, changedBoxrecUrl);

    updateBoxer.execute(primaryPort, params);

    FieldErrors errors = getFieldErrors();
    assertTrue(errors.hasMessage("name is mandatory"));
  }

  @Test
  public void givenANonExistingBoxerAndValidParamsThenIsInvalid() {
    BoxerId nonExistingBoxerId = new BoxerId("non-existing-id");
    UpdateBoxerParams params = new UpdateBoxerParams(nonExistingBoxerId, changedName, changedAlias, changedBoxrecUrl);

    updateBoxer.execute(primaryPort, params);

    FieldErrors errors = getFieldErrors();
    assertTrue(errors.hasMessage("Boxer: " + nonExistingBoxerId + " not found"));
  }

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }
}
