package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import com.danilat.scorecards.shared.PrimaryPort;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collection;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

public class BoxerSteps {

  @Autowired
  private BoxerRepository boxerRepository;

  @Autowired
  private RetrieveAllBoxers retrieveAllBoxers;

  private Collection<Boxer> retrievedBoxers;

  private PrimaryPort<Collection<Boxer>> primaryPortDouble() {
    return boxers -> retrievedBoxers = boxers;
  }

  @Given("an existing boxer called {string}")
  public void anExistingBoxerIs(String name) {
    BoxerId boxerId = new BoxerId(name);
    Boxer aBoxer = BoxerMother.aBoxerWithIdAndName(boxerId, name);
    boxerRepository.save(aBoxer);
  }

  @When("I retrieve all the boxers")
  public void iRetrieveAllTheBoxers() {
    retrieveAllBoxers.execute(primaryPortDouble());
  }

  @Then("the boxer called {string} is present")
  public void theBoxerCalledIsPresent(String name) {
    BoxerId boxerId = new BoxerId(name);
    Optional<Boxer> found = retrievedBoxers.stream().filter(boxer -> boxer.id().equals(boxerId)).findFirst();
    assertTrue(found.isPresent());
  }
}
