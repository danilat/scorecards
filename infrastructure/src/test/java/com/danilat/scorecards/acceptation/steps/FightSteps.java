package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.usecases.fights.RegisterFight;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Errors;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;

public class FightSteps {

  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;
  @Autowired
  private RetrieveAFight retrieveAFight;
  @Autowired
  private RegisterFight registerFight;
  @Autowired
  private World world;

  private Fight existingFight;
  private FightWithBoxers retrievedFight;
  private LocalDate aDate;
  private String aPlace;
  private final PrimaryPort<FightWithBoxers> retrieveFightPort = new PrimaryPort<FightWithBoxers>() {
    @Override
    public void success(FightWithBoxers fight) {
      retrievedFight = fight;
    }

    @Override
    public void error(Errors errors) {
      world.setErrors(errors);
    }
  };

  private PrimaryPort<Fight> getRegisterFightPort() {
    return new PrimaryPort<Fight>() {
      @Override
      public void success(Fight fight) {
        world.setFight(fight);
        world.setErrors(null);
      }

      @Override
      public void error(Errors errors) {
        world.setErrors(errors);
        world.setFight(null);
      }
    };
  }

  @Given("an existing fight")
  public void an_existing_fight() {
    existingFight = FightMother.aFightWithId("a fight");
    fightRepository.save(existingFight);
    Boxer firstBoxer = BoxerMother.aBoxerWithId(existingFight.firstBoxer());
    boxerRepository.save(firstBoxer);
    Boxer secondBoxer = BoxerMother.aBoxerWithId(existingFight.secondBoxer());
    boxerRepository.save(secondBoxer);
  }

  @When("I retrieve the existing fight")
  public void i_retrieve_the_existing_fight() {
    retrieveAFight.execute(existingFight.id(), retrieveFightPort);
  }

  @Then("the fight is present")
  public void the_fight_is_present() {
    assertEquals(existingFight.id(), retrievedFight.id());
  }

  @When("I retrieve a non-existing fight")
  public void i_retrieve_a_non_existing_fight() {
    retrieveAFight.execute(new FightId("some inexistent id"), retrieveFightPort);
  }

  @Then("the fight is not present")
  public void the_fight_is_not_present() {
    assertNull(retrievedFight);
  }

  @Given("an event in {string} at {string}")
  public void anEventInIn(String place, String date) throws ParseException {
    aDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
    aPlace = place;
  }

  @Given("an event in {string}")
  public void anEventIn(String place) {
    aPlace = place;
  }

  @Given("an event at {string}")
  public void anEventAt(String date) {
    aDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("d/MM/yyyy"));
  }

  @When("I register the fight in the event for {string} and {string} for {int} rounds")
  public void iRegisterTheFightInTheEventForAndForRounds(String firstBoxer, String secondBoxer,
      Integer numberOfRounds) {
    BoxerId firstBoxerId = new BoxerId(firstBoxer);
    BoxerId secondBoxerId = new BoxerId(secondBoxer);
    RegisterFightParameters parameters = new RegisterFightParameters(firstBoxerId, secondBoxerId,
        aDate, aPlace, numberOfRounds);

    registerFight.execute(parameters, getRegisterFightPort());
  }

  @When("I register the fight in the event for {string} and {string}")
  public void iRegisterTheFightInTheEventForAnd(String firstBoxer, String secondBoxer) {
    BoxerId firstBoxerId = new BoxerId(firstBoxer);
    BoxerId secondBoxerId = new BoxerId(secondBoxer);
    RegisterFightParameters parameters = new RegisterFightParameters(firstBoxerId, secondBoxerId,
        aDate, aPlace, null);

    registerFight.execute(parameters, getRegisterFightPort());
  }

  @Then("the fight is successfully registered")
  public void theFightIsRegistered() {
    assertNotNull(world.getFight());
    assertNotNull(world.getFight().id());
  }

  @Then("the fight is not registered")
  public void theFightIsNotRegistered() {
    assertNull(world.getFight());
    assertNotNull(world.getErrors());
  }

  @Given("an existing fight between {string} and {string} with {int} rounds")
  public void anExistingFightBetweenAndWithRounds(String firstBoxer, String secondBoxer, Integer numberOfRounds) {
    BoxerId firstBoxerId = new BoxerId(firstBoxer);
    BoxerId secondBoxerId = new BoxerId(secondBoxer);
    RegisterFightParameters parameters = new RegisterFightParameters(firstBoxerId, secondBoxerId,
        LocalDate.now(), "irrelevant place", numberOfRounds);

    registerFight.execute(parameters, getRegisterFightPort());
  }
}
