package com.danilat.scorecards.core.domain.score.projections;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.BoxerScores;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.shared.domain.Entity;
import java.time.Instant;
import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class ScoreCardWithFightDetails extends Entity<ScoreCardId> {

  private final FightId fightId;
  private final String place;
  private final LocalDate happenAt;
  private final Integer numberOfRounds;
  private final String firstBoxerName;
  private final String secondBoxerName;
  private final BoxerScores firstBoxerScores;
  private final BoxerScores secondBoxerScores;
  private final AccountId accountId;
  private final Instant scoredAt;

  public ScoreCardWithFightDetails(ScoreCardId id, FightId fightId, String place, LocalDate happenAt,
      Integer numberOfRounds, String firstBoxerName, String secondBoxerName, BoxerScores firstBoxerScores,
      BoxerScores secondBoxerScores, AccountId accountId, Instant scoredAt) {
    super(id);
    this.fightId = fightId;
    this.place = place;
    this.happenAt = happenAt;
    this.numberOfRounds = numberOfRounds;
    this.firstBoxerName = firstBoxerName;
    this.secondBoxerName = secondBoxerName;
    this.firstBoxerScores = firstBoxerScores;
    this.secondBoxerScores = secondBoxerScores;
    this.accountId = accountId;
    this.scoredAt = scoredAt;
  }

  public ScoreCardWithFightDetails(ScoreCard scoreCard, Fight fight, Boxer firstBoxer, Boxer secondBoxer) {
    super(scoreCard.id());
    this.fightId = scoreCard.fightId();
    this.place = fight.event().place();
    this.happenAt = fight.event().happenAt();
    this.numberOfRounds = fight.numberOfRounds();
    this.firstBoxerName = firstBoxer.name();
    this.secondBoxerName = secondBoxer.name();
    this.firstBoxerScores = scoreCard.firstBoxerScores();
    this.secondBoxerScores = scoreCard.secondBoxerScores();
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
    return firstBoxerScores.boxerId();
  }

  public String getFirstBoxerName() {
    return firstBoxerName;
  }

  public BoxerId getSecondBoxerId() {
    return secondBoxerScores.boxerId();
  }

  public String getSecondBoxerName() {
    return secondBoxerName;
  }

  public BoxerScores getFirstBoxerScores() {
    return firstBoxerScores;
  }

  public Integer getFirstBoxerScore() {
    return firstBoxerScores.total();
  }

  public BoxerScores getSecondBoxerScores() {
    return secondBoxerScores;
  }

  public Integer getSecondBoxerScore() {
    return secondBoxerScores.total();
  }

  public AccountId getAccountId() {
    return accountId;
  }

  public Instant getScoredAt() {
    return scoredAt;
  }
}
