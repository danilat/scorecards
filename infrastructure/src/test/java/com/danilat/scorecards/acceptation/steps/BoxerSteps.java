package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertNotNull;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

public class BoxerSteps {

  @Autowired
  private BoxerRepository boxerRepository;

  @Autowired
  private RetrieveAllBoxers retrieveAllBoxers;

  private Map<BoxerId, Boxer> boxers;

  @Given("an existing boxer called {string}")
  public void anExistingBoxerIs(String name) {
    BoxerId boxerId = new BoxerId(name);
    Boxer aBoxer = BoxerMother.aBoxerWithIdAndName(boxerId, name);
    boxerRepository.save(aBoxer);
  }

  @When("I retrieve all the boxers")
  public void iRetrieveAllTheBoxers() {
    boxers = retrieveAllBoxers.execute();
  }

  @Then("the boxer called {string} is present")
  public void theBoxerCalledIsPresent(String name) {
    BoxerId boxerId = new BoxerId(name);
    assertNotNull(boxers.get(boxerId));
  }
}
