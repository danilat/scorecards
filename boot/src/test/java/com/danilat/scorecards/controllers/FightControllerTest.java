package com.danilat.scorecards.controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.beans.HasPropertyWithValue.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.danilat.scorecards.controllers.FightController.ScoreForm;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.mothers.BoxerMother;
import com.danilat.scorecards.core.mothers.FightMother;
import com.danilat.scorecards.core.mothers.ScoreCardMother;
import com.danilat.scorecards.shared.Auth;
import java.time.Instant;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class FightControllerTest extends BaseControllerTest {

  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;
  @Autowired
  private ScoreCardRepository scoreCardRepository;
  @Autowired
  private Auth auth;

  private FightId fightId;
  private Boxer firstBoxer;
  private Boxer secondBoxer;
  private ScoreCard scoreCard;

  @Before
  public void setUp() {
    scoreCard = ScoreCardMother.aScoreCardWithIdAndAccount(ScoreCardMother.nextId(), auth.currentAccount());
    fightId = scoreCard.fightId();
    Fight fight = FightMother.aFightWithIdAndBoxers(fightId, scoreCard.firstBoxerId(), scoreCard.secondBoxerId());
    fightRepository.save(fight);
    firstBoxer = BoxerMother.aBoxerWithId(fight.firstBoxer());
    boxerRepository.save(firstBoxer);
    secondBoxer = BoxerMother.aBoxerWithId(fight.secondBoxer());
    boxerRepository.save(secondBoxer);
  }

  @Test
  public void getsAnExistingFight() throws Exception {
    this.mvc.perform(get("/fights/" + fightId.value()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("fight", notNullValue()))
        .andExpect(model().attribute("scores", notNullValue()))
        .andExpect(model().attribute("scores", hasProperty("firstBoxer", equalTo(firstBoxer.id().value()))))
        .andExpect(model().attribute("scores", hasProperty("secondBoxer", equalTo(secondBoxer.id().value()))));
    ;
  }

  @Test
  public void getsAnExistingFightWithScorecards() throws Exception {
    scoreCard.scoreRound(1, 10, 9, Instant.now());
    scoreCardRepository.save(scoreCard);

    this.mvc.perform(get("/fights/" + fightId.value()))
        .andExpect(status().isOk())
        .andExpect(model().attribute("fight", notNullValue()))
        .andExpect(model().attribute("scores", notNullValue()))
        .andExpect(model().attribute("scores", hasItem(firstBoxerScore(equalTo(10)))))
        .andExpect(model().attribute("scores", hasItem(secondBoxerScore(equalTo(9)))));
  }

  private FeatureMatcher<ScoreForm, Integer> firstBoxerScore(Matcher<Integer> matcher) {
    return new FeatureMatcher<ScoreForm, Integer>(matcher, "first boxer score", "firstBoxerScore") {
      @Override
      protected Integer featureValueOf(ScoreForm actual) {
        return actual.getFirstBoxerScore();
      }
    };
  }

  private FeatureMatcher<ScoreForm, Integer> secondBoxerScore(Matcher<Integer> matcher) {
    return new FeatureMatcher<ScoreForm, Integer>(matcher, "second boxer score", "secondBoxerScore") {
      @Override
      protected Integer featureValueOf(ScoreForm actual) {
        return actual.getSecondBoxerScore();
      }
    };
  }

  @Test
  public void getsANonExistingFight() throws Exception {
    String fightId = "non-existing-id";

    this.mvc.perform(get("/fights/" + fightId))
        .andExpect(status().isNotFound());
  }

  @Test
  public void postANewScoreWithValidParameters() throws Exception {
    this.mvc.perform(post("/fights/" + fightId.value() + "/score")
        .param("firstBoxer", firstBoxer.id().value())
        .param("firstBoxerScore", "10")
        .param("secondBoxer", secondBoxer.id().value())
        .param("secondBoxerScore", "10")
        .param("round", "1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("/fights/{fightId}"));
  }

  @Test
  public void postANewScoreWithInValidParameters() throws Exception {
    this.mvc.perform(post("/fights/" + fightId.value() + "/score")
        .param("firstBoxer", firstBoxer.id().value())
        .param("firstBoxerScore", "0")
        .param("secondBoxer", secondBoxer.id().value())
        .param("secondBoxerScore", "0")
        .param("round", "1"))
        .andExpect(status().isOk())
        .andExpect(view().name("show-fight"));
  }
}