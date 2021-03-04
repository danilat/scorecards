package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.events.RoundScored;
import com.danilat.scorecards.shared.domain.Entity;
import java.time.Instant;
import java.util.Objects;

public class ScoreCard extends Entity<ScoreCardId> {

  private final FightId fightId;
  private final BoxerScores firstBoxerScores;
  private final BoxerScores secondBoxerScores;
  private final AccountId accountId;
  private Instant scoredAt;

  public ScoreCard(ScoreCardId id, AccountId accountId, FightId fightId,
      BoxerScores firstBoxerScores, BoxerScores secondBoxerScores, Instant scoredAt) {
    super(id);
    this.fightId = fightId;
    this.accountId = accountId;
    this.firstBoxerScores = firstBoxerScores;
    this.secondBoxerScores = secondBoxerScores;
    this.scoredAt = scoredAt;
  }

  public static ScoreCard create(ScoreCardId id, AccountId accountId, FightId fightId, BoxerId firstBoxerId,
      BoxerId secondBoxerId) {
    return new ScoreCard(id, accountId, fightId, new BoxerScores(firstBoxerId), new BoxerScores(
        secondBoxerId),
        null);
  }

  @Override
  public ScoreCardId id() {
    return id;
  }

  public FightId fightId() {
    return fightId;
  }

  public BoxerId firstBoxerId() {
    return firstBoxerScores.boxerId();
  }

  public BoxerId secondBoxerId() {
    return secondBoxerScores.boxerId();
  }

  public AccountId accountId() {
    return accountId;
  }

  public Integer firstBoxerScore() {
    return firstBoxerScores.total();
  }

  public Integer firstBoxerScore(int round) {
    return firstBoxerScores.inRound(round);
  }

  public Integer secondBoxerScore() {
    return secondBoxerScores.total();
  }

  public Integer secondBoxerScore(int round) {
    return secondBoxerScores.inRound(round);
  }

  public BoxerScores firstBoxerScores() {
    return firstBoxerScores;
  }

  public BoxerScores secondBoxerScores() {
    return secondBoxerScores;
  }

  public void scoreRound(int round, int firstBoxerScore, int secondBoxerScore, Instant scoredAt) {
    firstBoxerScores.add(round, firstBoxerScore);
    secondBoxerScores.add(round, secondBoxerScore);
    this.scoredAt = scoredAt;
    addDomainEvent(RoundScored.create(this, scoredAt));
  }

  public Instant scoredAt() {
    return scoredAt;
  }

  public boolean isRoundScored(int round) {
    return firstBoxerScore(round) != null && secondBoxerScore(round) != null;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    ScoreCard scoreCard = (ScoreCard) o;
    return Objects.equals(fightId, scoreCard.fightId) &&
        Objects.equals(firstBoxerScores, scoreCard.firstBoxerScores) &&
        Objects.equals(secondBoxerScores, scoreCard.secondBoxerScores) &&
        Objects.equals(accountId, scoreCard.accountId) &&
        Objects.equals(scoredAt, scoreCard.scoredAt);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(super.hashCode(), fightId, firstBoxerScores, secondBoxerScores, accountId,
            scoredAt);
  }

  @Override
  public String toString() {
    return "ScoreCard{" +
        "fightId=" + fightId +
        ", firstBoxerScores=" + firstBoxerScores +
        ", secondBoxerScores=" + secondBoxerScores +
        ", accountId=" + accountId +
        ", scoredAt=" + scoredAt +
        ", id=" + id +
        '}';
  }
}
