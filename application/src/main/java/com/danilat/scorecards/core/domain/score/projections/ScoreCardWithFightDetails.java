package com.danilat.scorecards.core.domain.score.projections;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.shared.domain.Entity;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;

public class ScoreCardWithFightDetails extends Entity<ScoreCardId> {

  private final FightId fightId;
  private final String place;
  private final LocalDate happenAt;
  private final Integer numberOfRounds;
  private final BoxerId firstBoxerId;
  private final String firstBoxerName;
  private final BoxerId secondBoxerId;
  private final String secondBoxerName;
  private final HashMap<Integer, Integer> firstBoxerScores;
  private final Integer firstBoxerScore;
  private final HashMap<Integer, Integer> secondBoxerScores;
  private final Integer secondBoxerScore;
  private final AccountId accountId;
  private final Instant scoredAt;

  public ScoreCardWithFightDetails(ScoreCard scoreCard, Fight fight, Boxer firstBoxer, Boxer secondBoxer) {
    super(scoreCard.id());
    this.fightId = scoreCard.fightId();
    this.place = fight.event().place();
    this.happenAt = fight.event().happenAt();
    this.numberOfRounds = fight.numberOfRounds();
    this.firstBoxerId = scoreCard.firstBoxerId();
    this.firstBoxerName = firstBoxer.name();
    this.secondBoxerId = scoreCard.secondBoxerId();
    this.secondBoxerName = secondBoxer.name();
    this.firstBoxerScores = scoreCard.firstBoxerScores();
    this.firstBoxerScore = scoreCard.firstBoxerScore();
    this.secondBoxerScores = scoreCard.secondBoxerScores();
    this.secondBoxerScore = scoreCard.secondBoxerScore();
    this.accountId = scoreCard.accountId();
    this.scoredAt = scoreCard.scoredAt();

  }

  public FightId getFightId() {
    return fightId;
  }

  public String getPlace() {
    return place;
  }

  public LocalDate getHappenAt() {
    return happenAt;
  }

  public Integer getNumberOfRounds() {
    return numberOfRounds;
  }

  public BoxerId getFirstBoxerId() {
    return firstBoxerId;
  }

  public String getFirstBoxerName() {
    return firstBoxerName;
  }

  public BoxerId getSecondBoxerId() {
    return secondBoxerId;
  }

  public String getSecondBoxerName() {
    return secondBoxerName;
  }

  public HashMap<Integer, Integer> getFirstBoxerScores() {
    return firstBoxerScores;
  }

  public Integer getFirstBoxerScore() {
    return firstBoxerScore;
  }

  public HashMap<Integer, Integer> getSecondBoxerScores() {
    return secondBoxerScores;
  }

  public Integer getSecondBoxerScore() {
    return secondBoxerScore;
  }

  public AccountId getAccountId() {
    return accountId;
  }

  public Instant getScoredAt() {
    return scoredAt;
  }
}
