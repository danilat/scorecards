package com.danilat.scorecards.core.usecases.boxers;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveAllBoxersTest extends UseCaseUnitTest<Map<BoxerId, Boxer>> {

  @Mock
  private BoxerRepository boxerRepository;

  @Mock
  PrimaryPort<Map<BoxerId, Boxer>> primaryPort;

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  @Test
  public void givenNoBoxersThenIsEmpty() {
    when(boxerRepository.all()).thenReturn(new HashMap());
    RetrieveAllBoxers retrieveAllBoxers = new RetrieveAllBoxers(boxerRepository);

    retrieveAllBoxers.execute(primaryPort);

    Map<BoxerId, Boxer> boxers = getSuccessEntity();
    assertEquals(0, boxers.size());
  }

  @Test
  public void givenSomeBoxerTheAreRetrieved() {
    Boxer aBoxer = BoxerMother.aBoxerWithId("Pacquiao");
    Map<BoxerId, Boxer> existingBoxers = new HashMap<>();
    existingBoxers.put(aBoxer.id(), aBoxer);
    when(boxerRepository.all()).thenReturn(existingBoxers);
    RetrieveAllBoxers retrieveAllBoxers = new RetrieveAllBoxers(boxerRepository);

    retrieveAllBoxers.execute(primaryPort);

    Map<BoxerId, Boxer> boxers = getSuccessEntity();
    assertEquals(existingBoxers, boxers);
  }
}
