package com.danilat.scorecards.acceptation.steps;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.usecases.fights.ScoreRound;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ScoreSteps {
    @Autowired
    private ScoreRound scoreRound;
    @Autowired
    private World world;

    private ScoreCard scoreCard;

    @When("an aficionado scores the round {int} for the existing fight with {int} and {int}")
    public void anAficionadoScoresTheRoundForTheExistingFightWithAnd(Integer round, Integer firstBoxerScore, Integer secondBoxerScore) {
        Fight fight = world.getFight();
        ScoreRound.ScoreFightParameters params = new ScoreRound.ScoreFightParameters(fight.id(), round, fight.firstBoxer(), firstBoxerScore, fight.secondBoxer(), secondBoxerScore);
        scoreCard = scoreRound.execute(params);
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
    public void theExistingFightHasBeenScoredByTheAficionadoInTheRoundWithAnd(Integer round, Integer firstBoxerScore, Integer secondBoxerScore) {
        Fight fight = world.getFight();
        ScoreRound.ScoreFightParameters params = new ScoreRound.ScoreFightParameters(fight.id(), round, fight.firstBoxer(), firstBoxerScore, fight.secondBoxer(), secondBoxerScore);
        scoreCard = scoreRound.execute(params);
    }

    @When("an aficionado scores the round {int} for the non-existing fight with {int} and {int}")
    public void anAficionadoScoresTheRoundForTheNonExistingFightWithAnd(Integer int1, Integer int2, Integer int3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the round is not scored")
    public void theRoundIsNotScored() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("an aficionado scores the round {int} for the existing fight with {int} for the first boxer")
    public void anAficionadoScoresTheRoundForTheExistingFightWithForTheFirstBoxer(Integer int1, Integer int2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
