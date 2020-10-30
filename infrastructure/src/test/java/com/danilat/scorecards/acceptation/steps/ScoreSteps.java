package com.danilat.scorecards.acceptation.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ScoreSteps {
    @When("an aficionado scores the round {string} for the existing fight with {string} and {string}")
    public void anAficionadoScoresTheRoundForTheExistingFightWithAnd(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the round is scored with with {string} and {string}")
    public void theRoundIsScoredWithWithAnd(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    @Then("the fight score is {string} to {string}")
    public void theFightScoreIsTo(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("an aficionado scores without none round")
    public void anAficionadoScoresWithoutNoneRound() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("an aficionado scores the round {string} for the non-existing fight with {string} and {string}")
    public void anAficionadoScoresTheRoundForTheNonExistingFightWithAnd(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("the fight is not scored")
    public void theFightIsNotScored() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("the existing fight has been scored by the aficionado in the round {string} with {string} and {string}")
    public void theExistingFightHasBeenScoredByTheAficionadoInTheRoundWithAnd(String string, String string2, String string3) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
