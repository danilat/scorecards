package com.danilat.scorecards.core.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.persistence.jdbc.JdbcBoxerRepository;
import com.danilat.scorecards.core.persistence.jdbc.JdbcConfig;
import java.util.Collection;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {JdbcBoxerRepository.class, JdbcConfig.class})
@RunWith(SpringRunner.class)
public class BoxerRepositoryTest {

  @Autowired
  private BoxerRepository boxerRepository;
  private BoxerId boxerId = new BoxerId("some irrelevant id");

  @Before
  public void setUp(){
    boxerRepository.clear();
  }


  @Test
  public void saveANewBoxer() {
    Boxer aBoxer = BoxerMother.aBoxerWithId(boxerId);

    boxerRepository.save(aBoxer);

    Optional retrieved = boxerRepository.get(boxerId);
    assertTrue(retrieved.isPresent());
    assertEquals(retrieved.get(), aBoxer);
  }

  public void saveAnExistingBoxer() {
    Boxer aBoxer = BoxerMother.aBoxerWithId(boxerId);
    boxerRepository.save(aBoxer);

    aBoxer = new Boxer(boxerId, "Pepito, The Beast");
    boxerRepository.save(aBoxer);

    Boxer retrieved = boxerRepository.get(boxerId).get();
    assertEquals("Pepito, The Beast", retrieved.name());
  }

  @Test
  public void getAllBoxers() {
    Boxer aBoxer = BoxerMother.aBoxerWithId(boxerId);
    boxerRepository.save(aBoxer);

    Collection<Boxer> boxers = boxerRepository.all();

    assertEquals(1, boxers.size());
  }

}
