package com.danilat.scorecards.core.usecases.boxers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RetrieveAllBoxersTest {

  @Mock
  private BoxerRepository boxerRepository;

  @Test
  public void givenNoBoxersThenIsEmpty() {
    when(boxerRepository.all()).thenReturn(new HashMap());
    RetrieveAllBoxers retrieveAllBoxers = new RetrieveAllBoxers(boxerRepository);

    Map<BoxerId, Boxer> boxers = retrieveAllBoxers.execute();

    assertEquals(0, boxers.size());
  }

  @Test
  public void givenSomeBoxerTheAreRetrieved() {
    Boxer aBoxer = BoxerMother.aBoxerWithId("Pacquiao");
    Map<BoxerId, Boxer> existingBoxers = new HashMap<>();
    existingBoxers.put(aBoxer.id(), aBoxer);
    when(boxerRepository.all()).thenReturn(existingBoxers);
    RetrieveAllBoxers retrieveAllBoxers = new RetrieveAllBoxers(boxerRepository);

    Map<BoxerId, Boxer> boxers = retrieveAllBoxers.execute();

    assertEquals(existingBoxers, boxers);
  }

}
