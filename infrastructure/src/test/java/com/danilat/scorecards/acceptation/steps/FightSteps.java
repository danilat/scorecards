package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.danilat.scorecards.core.domain.ScoreCardsBusinessException;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.repositories.InMemoryBoxerRepository;
import com.danilat.scorecards.repositories.InMemoryFightRepository;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FightSteps {

  private Fight existingFight;
  private Fight retrievedFight;
  private FightRepository fightRepository;
  private Exception someException;
  private BoxerRepository boxerRepository;
  private LocalDate aDate;
  private String aPlace;

  @Before
  public void setUp() {
    fightRepository = new InMemoryFightRepository();
    boxerRepository = new InMemoryBoxerRepository();
  }

  @Given("an existing fight")
  public void an_existing_fight() {
    existingFight = FightMother.aFightWithId("a fight");
    fightRepository.save(existingFight);
  }

  @When("I retrieve the existing fight")
  public void i_retrieve_the_existing_fight() {
    RetrieveAFight retrieveAFight = new RetrieveAFight(fightRepository);
    retrievedFight = retrieveAFight.execute(existingFight.id());
  }

  @Then("the fight is present")
  public void the_fight_is_present() {
    assertEquals(existingFight, retrievedFight);
  }

  @When("I retrieve a non-existing fight")
  public void i_retrieve_a_non_existing_fight() {
    try {
      RetrieveAFight retrieveAFight = new RetrieveAFight(fightRepository);
      retrievedFight = retrieveAFight.execute(new FightId("some inexistent id"));
    } catch (ScoreCardsBusinessException businessException) {
      someException = businessException;
    }
    assertNotNull(someException);
  }

  @Then("the fight is not present")
  public void the_fight_is_not_present() {
    assertNull(retrievedFight);
  }

  @Given("an existing boxer called {string}")
  public void anExistingBoxerCalled(String name) {
    BoxerId boxerId = new BoxerId(name);
    Boxer aBoxer = BoxerMother.aBoxerWithIdAndName(boxerId, name);
    boxerRepository.save(aBoxer);
  }

  @Given("an event in {string} at {string}")
  public void anEventInAt(String place, String date) {
    aDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    aPlace = place;
  }

  @When("I register the fight in the event for {string} and {string} for {int} rounds")
  public void iRegisterTheFightInTheEventForAndForRounds(String firstBoxerName, String secondBoxerName,
      Integer numberOfRounds) {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
  }

  @Then("the fight is successfully registered")
  public void theFightIsSuccessfullyRegistered() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
  }

  @Given("an event at {string}")
  public void anEventAt(String date) {
    aDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
  }

  @Given("an event in {string}")
  public void anEventIn(String place) {
    aPlace = place;
  }

  @Then("the fight is not registered")
  public void theFightIsNotRegistered() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
  }

  @When("I register the fight in the event for {string} and {string}")
  public void iRegisterTheFightInTheEventForAnd(String firstBoxerName, String secondBoxerName) {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
  }
}
