package com.danilat.scorecards.acceptation.steps;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.core.usecases.scores.RetrieveAScoreCard;
import com.danilat.scorecards.core.usecases.scores.RetrieveAScoreCard.RetrieveAScoreCardParameters;
import com.danilat.scorecards.core.usecases.scores.RetrieveScoreCards;
import com.danilat.scorecards.core.usecases.scores.ScoreRound;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.FieldErrors;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Collection;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class ScoreSteps {

  @Autowired
  private World world;
  @Autowired
  private ScoreCardRepository scoreCardRepository;
  @Autowired
  private BoxerRepository boxerRepository;
  @Autowired
  private FightRepository fightRepository;

  @Autowired
  private ScoreRound scoreRound;
  private ScoreCard scoreCard;
  private FieldErrors someErrors;
  private PrimaryPort<ScoreCard> scoreRoundPrimaryPort = new PrimaryPort<ScoreCard>() {
    @Override
    public void success(ScoreCard entity) {
      scoreCard = entity;
      someErrors = null;
    }

    @Override
    public void error(FieldErrors errors) {
      someErrors = errors;
      scoreCard = null;
    }
  };

  @Autowired
  private RetrieveScoreCards retrieveScoreCards;
  private Collection<ScoreCardWithFightDetails> scoreCardWithFightDetails;
  private PrimaryPort<Collection<ScoreCardWithFightDetails>> retrieveScoreCardsPrimaryPort = new PrimaryPort<Collection<ScoreCardWithFightDetails>>() {
    @Override
    public void success(Collection<ScoreCardWithFightDetails> response) {
      scoreCardWithFightDetails = response;
    }
  };

  @Autowired
  private RetrieveAScoreCard retrieveAScoreCard;
  private ScoreCardId existingScoreCardId;
  private ScoreCard existingScoreCard;
  private PrimaryPort<ScoreCard> retrieveAScoreCardPrimaryPort = new PrimaryPort<ScoreCard>() {
    @Override
    public void success(ScoreCard response) {
      scoreCard = response;
    }

    @Override
    public void error(FieldErrors errors) {
      someErrors = errors;
      scoreCard = null;
    }
  };

  @When("an aficionado scores the round {int} for the existing fight with {int} and {int}")
  public void anAficionadoScoresTheRoundForTheExistingFightWithAnd(Integer round, Integer firstBoxerScore,
      Integer secondBoxerScore) {
    Fight fight = world.getFight();
    ScoreRound.ScoreFightParameters params = new ScoreRound.ScoreFightParameters(fight.id(), round, fight.firstBoxer(),
        firstBoxerScore, fight.secondBoxer(), secondBoxerScore);

    scoreRound.execute(scoreRoundPrimaryPort, params);
  }

  @Then("the round {int} is scored with with {int} and {int}")
  public void theRoundIsScoredWithWithAnd(Integer round, Integer firstBoxerScore, Integer secondBoxerScore) {
    assertEquals(firstBoxerScore, scoreCard.firstBoxerScore(round));
    assertEquals(secondBoxerScore, scoreCard.secondBoxerScore(round));
  }

  @Then("the fight scoreCard is {int} to {int}")
  public void theFightScoreCardIsTo(Integer firstBoxerScore, Integer secondBoxerScore) {
    assertEquals(firstBoxerScore, scoreCard.firstBoxerScore());
    assertEquals(secondBoxerScore, scoreCard.secondBoxerScore());
  }

  @Given("the existing fight has been scored by the aficionado in the round {int} with {int} and {int}")
  public void theExistingFightHasBeenScoredByTheAficionadoInTheRoundWithAnd(Integer round, Integer firstBoxerScore,
      Integer secondBoxerScore) {
    Fight fight = world.getFight();
    ScoreRound.ScoreFightParameters params = new ScoreRound.ScoreFightParameters(fight.id(), round, fight.firstBoxer(),
        firstBoxerScore, fight.secondBoxer(), secondBoxerScore);

    scoreRound.execute(scoreRoundPrimaryPort, params);
  }

  @When("an aficionado scores the round {int} for the non-existing fight with {int} and {int}")
  public void anAficionadoScoresTheRoundForTheNonExistingFightWithAnd(Integer round, Integer firstBoxerScore,
      Integer secondBoxerScore) {
    FightId nonExistingFightId = new FightId("not-exists");
    ScoreRound.ScoreFightParameters params = new ScoreRound.ScoreFightParameters(nonExistingFightId, round,
        new BoxerId("irrelevant 1"), firstBoxerScore, new BoxerId("irrelevant 2"), secondBoxerScore);

    scoreRound.execute(scoreRoundPrimaryPort, params);
  }

  @Then("the round is not scored")
  public void theRoundIsNotScored() {
    assertNotNull(someErrors);
    assertNull(scoreCard);
  }

  @When("an aficionado scores the round {int} for the existing fight with {int} for the first boxer")
  public void anAficionadoScoresTheRoundForTheExistingFightWithForTheFirstBoxer(Integer round,
      Integer firstBoxerScore) {
    Fight fight = world.getFight();
    ScoreRound.ScoreFightParameters params = new ScoreRound.ScoreFightParameters(fight.id(), round, fight.firstBoxer(),
        firstBoxerScore, fight.secondBoxer(), null);

    scoreRound.execute(scoreRoundPrimaryPort, params);
  }

  @When("an aficionado scores the round {int} for the existing fight with {int} for {string} and {int} for {string}")
  public void anAficionadoScoresTheRoundForTheExistingFightWithForAndFor(Integer round, Integer firstBoxerScore,
      String firstBoxer, Integer secondBoxerScore, String secondBoxer) {
    Fight fight = world.getFight();
    ScoreRound.ScoreFightParameters params = new ScoreRound.ScoreFightParameters(fight.id(), round,
        new BoxerId(firstBoxer), firstBoxerScore, new BoxerId(secondBoxer), secondBoxerScore);

    scoreRound.execute(scoreRoundPrimaryPort, params);
  }

  @Given("{string} account has scored {int} fights")
  public void accountHasScoredFights(String account, Integer numberOfScorecards) {
    IntStream.range(0, numberOfScorecards).forEach(index -> {
      ScoreCard scoreCard = ScoreCardMother
          .aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), new AccountId(account));
      boxerRepository.save(BoxerMother.aBoxerWithId(scoreCard.firstBoxerId()));
      boxerRepository.save(BoxerMother.aBoxerWithId(scoreCard.secondBoxerId()));
      fightRepository.save(FightMother
          .aFightWithIdAndBoxers(scoreCard.fightId(), scoreCard.firstBoxerId(),
              scoreCard.secondBoxerId()));
      scoreCardRepository
          .save(scoreCard);
    });
  }

  @When("I retrieve the scorecards for {string}")
  public void iRetrieveTheScorecardsFor(String account) {
    retrieveScoreCards.execute(retrieveScoreCardsPrimaryPort, new AccountId(account));
  }

  @Then("scorecards that {string} did are present")
  public void scorecardsThatDidArePresent(String account) {
    boolean allAreForTheAccount = scoreCardWithFightDetails.stream()
        .allMatch(scoreCard -> scoreCard.getAccountId().equals(new AccountId(account)));
    assertTrue(allAreForTheAccount);
  }

  @Then("scorecards that {string} did are not present")
  public void scorecardsThatDidAreNotPresent(String account) {
    boolean someIsForTheAccount = scoreCardWithFightDetails.stream()
        .anyMatch(scoreCard -> scoreCard.getAccountId().equals(new AccountId(account)));
    assertFalse(someIsForTheAccount);
  }

  @Given("an existing scorecard")
  public void anExistingScorecard() {
    existingScoreCardId = ScoreCardMother.nextId();
    existingScoreCard = ScoreCardMother.aScoreCardWithId(existingScoreCardId);
    scoreCardRepository.save(existingScoreCard);
  }

  @When("I retrieve the existing scorecard by it's fight and account")
  public void iRetrieveTheExistingScorecardByTheFightAndTheAccount() {
    retrieveAScoreCard.execute(retrieveAScoreCardPrimaryPort,
        new RetrieveAScoreCardParameters(existingScoreCard.fightId(), existingScoreCard.accountId()));
  }

  @Then("the scorecard is present")
  public void theScorecardIsPresent() {
    assertNotNull(scoreCard);
    assertNull(someErrors);
  }

  @When("I retrieve a scorecard by a fight and account that not exist")
  public void iRetrieveAScorecardByAFightAndAccountThatNotExist() {
    retrieveAScoreCard.execute(retrieveAScoreCardPrimaryPort,
        new RetrieveAScoreCardParameters(new FightId("foo"), new AccountId("bar")));
  }

  @Then("the scorecard is not present")
  public void theScorecardIsNotPresent() {
    assertNull(scoreCard);
    assertNotNull(someErrors);
  }
}
