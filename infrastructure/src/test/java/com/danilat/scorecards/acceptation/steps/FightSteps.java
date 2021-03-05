package com.danilat.scorecards.acceptation.steps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import com.danilat.scorecards.core.usecases.fights.RetrieveLastPastFights;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Errors;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
  private RetrieveLastPastFights retrieveLastPastFights;
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
  private Collection<FightWithBoxers> retrievedFights;
  private PrimaryPort<Collection<FightWithBoxers>> getRetrieveLastPastFightsPort() {
    return new PrimaryPort<Collection<FightWithBoxers>>() {
      @Override
      public void success(Collection<FightWithBoxers> response) {
        retrievedFights = response;
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
    retrieveAFight.execute(retrieveFightPort, existingFight.id());
  }

  @Then("the fight is present")
  public void the_fight_is_present() {
    assertEquals(existingFight.id(), retrievedFight.id());
  }

  @When("I retrieve a non-existing fight")
  public void i_retrieve_a_non_existing_fight() {
    retrieveAFight.execute(retrieveFightPort, new FightId("some inexistent id"));
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

    registerFight.execute(getRegisterFightPort(), parameters);
  }

  @When("I register the fight in the event for {string} and {string}")
  public void iRegisterTheFightInTheEventForAnd(String firstBoxer, String secondBoxer) {
    BoxerId firstBoxerId = new BoxerId(firstBoxer);
    BoxerId secondBoxerId = new BoxerId(secondBoxer);
    RegisterFightParameters parameters = new RegisterFightParameters(firstBoxerId, secondBoxerId,
        aDate, aPlace, null);

    registerFight.execute(getRegisterFightPort(), parameters);
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

    registerFight.execute(getRegisterFightPort(), parameters);
  }

  @Given("exists a fight that happened {string}")
  public void existsAFightThatHappened(String happen) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    existingFight = FightMother.aFightWithHappenAt(LocalDate.parse(happen, formatter));
    fightRepository.save(existingFight);
    Boxer firstBoxer = BoxerMother.aBoxerWithId(existingFight.firstBoxer());
    boxerRepository.save(firstBoxer);
    Boxer secondBoxer = BoxerMother.aBoxerWithId(existingFight.secondBoxer());
    boxerRepository.save(secondBoxer);
  }

  @When("I retrieve the last fights")
  public void iRetrieveTheLastFights() {
    retrieveLastPastFights.execute(getRetrieveLastPastFightsPort());
  }
  @Then("the fights are present")
  public void theFightsArePresent() {
    assertNotNull(retrievedFights);
    assertTrue(retrievedFights.size() > 0);
  }
  @Then("the fight that happened {string} is first than the fight that happened {string}")
  public void theFightThatHappenedIsFirstThanTheFightThatHappened(String firstFightDate, String secondFightDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    List<FightWithBoxers> fights = new ArrayList<>(retrievedFights);
    assertEquals(LocalDate.parse(firstFightDate, formatter), fights.get(0).getHappenAt());
    assertEquals(LocalDate.parse(secondFightDate, formatter), fights.get(1).getHappenAt());
  }

  @Given("there is no fights")
  public void thereIsNoFights() {
    fightRepository.clear();
  }

  @Then("the fights are not present")
  public void theFightsAreNotPresent() {
    assertNotNull(retrievedFights);
    assertEquals(0, retrievedFights.size());
  }
}
