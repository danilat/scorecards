package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertEquals;

import com.danilat.scorecards.acceptation.repositories.InMemoryFightRepository;
import com.danilat.scorecards.core.domain.Fight;
import com.danilat.scorecards.core.domain.FightRepository;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.Optional;

public class FightSteps {
  Fight existingFight;
  private Fight retrievedFight;


  @Given("an existing fight")
  public void an_existing_fight() {
    existingFight = FightMother.aFightWithId("a fight");


  }

  @When("I retrieve the existing fight")
  public void i_retrieve_the_existing_fight() {
    FightRepository fightRepository = new InMemoryFightRepository() ;
    RetrieveAFight retrieveAFight = new RetrieveAFight(fightRepository);
    retrievedFight = retrieveAFight.execute(existingFight.id());
  }

  @Then("the fight is present")
  public void the_fight_is_present() {
    assertEquals(existingFight, retrievedFight);
  }

  @When("I retrieve a non-existing fight")
  public void i_retrieve_a_non_existing_fight() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
  }

  @Then("the fight is not present")
  public void the_fight_is_not_present() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
  }
}
