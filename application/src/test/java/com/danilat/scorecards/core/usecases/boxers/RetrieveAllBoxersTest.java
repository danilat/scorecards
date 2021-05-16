package com.danilat.scorecards.core.usecases.boxers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.usecases.UseCaseUnitTest;
import com.danilat.scorecards.shared.PrimaryPort;
import java.util.ArrayList;
import java.util.Collection;

import com.danilat.scorecards.shared.domain.Sort;
import org.junit.Test;
import org.mockito.Mock;

public class RetrieveAllBoxersTest extends UseCaseUnitTest<Collection<Boxer>> {

  @Mock
  private BoxerRepository boxerRepository;

  @Mock
  PrimaryPort<Collection<Boxer>> primaryPort;

  @Override
  public PrimaryPort getPrimaryPort() {
    return primaryPort;
  }

  Sort sort = new Sort("name", Sort.Direction.ASC);

  @Test
  public void givenNoBoxersThenIsEmpty() {
    when(boxerRepository.all(sort)).thenReturn(new ArrayList<>());
    RetrieveAllBoxers retrieveAllBoxers = new RetrieveAllBoxers(boxerRepository);

    retrieveAllBoxers.execute(primaryPort);

    Collection<Boxer> boxers = getSuccessEntity();
    assertEquals(0, boxers.size());
  }

  @Test
  public void givenSomeBoxerTheAreRetrieved() {
    Boxer aBoxer = BoxerMother.aBoxerWithId("Pacquiao");
    Collection<Boxer> existingBoxers = new ArrayList<>();
    existingBoxers.add(aBoxer);
    when(boxerRepository.all(sort)).thenReturn(existingBoxers);
    RetrieveAllBoxers retrieveAllBoxers = new RetrieveAllBoxers(boxerRepository);

    retrieveAllBoxers.execute(primaryPort);

    Collection<Boxer> boxers = getSuccessEntity();
    assertEquals(existingBoxers, boxers);
  }
}
