package com.danilat.scorecards.core.usecases.boxers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundError;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.Error;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveABoxerTest extends UseCaseUnitTest<Boxer> {

  @Mock
  private BoxerRepository boxerRepository;

  @Mock
  private PrimaryPort primaryPort;

  private BoxerId boxerId = new BoxerId("an id");
  private Boxer existingBoxer = BoxerMother.aBoxerWithId(boxerId);
  private RetrieveABoxer retrieveABoxer;

  @Before
  public void setup() {
    when(boxerRepository.get(boxerId)).thenReturn(Optional.of(existingBoxer));
    retrieveABoxer = new RetrieveABoxer(boxerRepository);
  }

  @Test
  public void givenAnExistingBoxerThenIsRetrieved() {
    retrieveABoxer.execute(getPrimaryPort(), boxerId);

    Boxer boxer = getSuccessEntity();
    assertEquals(existingBoxer, boxer);
  }

  @Test
  public void givenAnNonExistingBoxerThenIsNotRetrieved() {
    retrieveABoxer.execute(primaryPort, new BoxerId("un-existing id"));

    Error errors = getError();
    assertTrue(errors instanceof BoxerNotFoundError);
  }

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }
}
