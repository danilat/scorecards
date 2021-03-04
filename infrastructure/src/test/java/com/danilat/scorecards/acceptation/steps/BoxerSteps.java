package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer.CreateBoxerParams;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.FieldErrors;
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

  private PrimaryPort<Collection<Boxer>> primaryPortRetrieveAllBoxers() {
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
    retrieveAllBoxers.execute(primaryPortRetrieveAllBoxers());
  }

  @Then("the boxer called {string} is present")
  public void theBoxerCalledIsPresent(String name) {
    BoxerId boxerId = new BoxerId(name);
    Optional<Boxer> found = retrievedBoxers.stream().filter(boxer -> boxer.id().equals(boxerId)).findFirst();
    assertTrue(found.isPresent());
  }

  private String name;
  private String alias;
  private String boxrecUrl;

  private Boxer boxer;
  private FieldErrors fieldErrors;

  @Autowired
  private CreateBoxer createBoxer;
  private PrimaryPort<Boxer> primaryPortCreateBoxer = new PrimaryPort<Boxer>() {
    @Override
    public void success(Boxer response) {
      boxer = response;
    }
  };

  @Given("a boxer name {string}")
  public void aBoxerName(String name) {
    this.name = name;
  }

  @Given("a boxer alias {string}")
  public void aBoxerAlias(String alias) {
    this.alias = alias;
  }
  @Given("a boxrec url {string}")
  public void aBoxrecUrl(String boxrecUrl) {
    this.boxrecUrl = boxrecUrl;
  }
  @When("I create the boxer")
  public void iCreateTheBoxer() {
    CreateBoxerParams params = new CreateBoxerParams(name, alias, boxrecUrl);
    createBoxer.execute(primaryPortCreateBoxer, params);
  }
  @Then("the boxer is successfully created")
  public void theBoxerIsSuccessfullyCreated() {
    assertNotNull(boxer);
    assertNull(fieldErrors);
  }

  @Then("the boxer is not created")
  public void theBoxerIsNotCreated() {
    assertNull(boxer);
    assertNotNull(fieldErrors);
  }
}
