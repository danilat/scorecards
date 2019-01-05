package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundException;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundException;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.usecases.fights.InvalidFightException;
import com.danilat.scorecards.core.usecases.fights.RegisterFight;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.repositories.InMemoryBoxerRepository;
import com.danilat.scorecards.repositories.InMemoryFightRepository;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FightSteps {

  private Fight existingFight;
  private Fight retrievedFight;
  private FightRepository fightRepository;

  private LocalDate aDate;
  private BoxerRepository boxerRepository;
  private Fight createdFight;
  private String aPlace;
  private Exception someException;

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
    } catch (FightNotFoundException exception) {
    }
  }

  @Then("the fight is not present")
  public void the_fight_is_not_present() {
    assertNull(retrievedFight);
  }

  @Given("an existing boxer called {string}")
  public void anExistingBoxerIs(String name) {
    BoxerId boxerId = new BoxerId(name);
    Boxer aBoxer = BoxerMother.aBoxerWithIdAndName(boxerId, name);
    boxerRepository.save(aBoxer);
  }


  @Given("an event in {string} at {string}")
  public void anEventInIn(String place, String date) throws ParseException {
    aDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    aPlace = place;
  }

  @When("I register the fight in the event for {string} and {string}")
  public void iRegisterTheFightInTheEventForAnd(String firstBoxer, String secondBoxer) {
    RegisterFight registerFight = new RegisterFight(fightRepository, boxerRepository);
    BoxerId firstBoxerId = new BoxerId(firstBoxer);
    BoxerId secondBoxerId = new BoxerId(secondBoxer);
    RegisterFightParameters parameters = new RegisterFightParameters(firstBoxerId, secondBoxerId,
        aDate, aPlace);

    try {
      createdFight = registerFight.execute(parameters);
    } catch (BoxerNotFoundException boxerNotFound) {
      someException = boxerNotFound;
    } catch (InvalidFightException invalidFight) {
      someException = invalidFight;
    }
  }

  @Then("the fight is successfully registered")
  public void theFightIsRegistered() {
    assertNotNull(createdFight);
    assertNotNull(createdFight.id());
  }

  @Then("the fight is not registered")
  public void theFightIsNotRegistered() {
    assertNull(createdFight);
    assertNotNull(someException);
  }
}
