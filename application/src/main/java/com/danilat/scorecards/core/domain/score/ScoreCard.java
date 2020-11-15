package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.events.RoundScored;
import com.danilat.scorecards.shared.domain.Entity;
import java.time.Instant;
import java.util.HashMap;

public class ScoreCard extends Entity<ScoreCardId> {

  private final FightId fightId;
  private final BoxerId firstBoxerId;
  private final BoxerId secondBoxerId;
  private final HashMap<Integer, Integer> firstBoxerScores;
  private final HashMap<Integer, Integer> secondBoxerScores;
  private final AccountId accountId;
  private Instant scoredAt;

  public ScoreCard(ScoreCardId id, AccountId accountId, FightId fightId, BoxerId firstBoxerId, BoxerId secondBoxerId,
      HashMap<Integer, Integer> firstBoxerScores, HashMap<Integer, Integer> secondBoxerScores) {
    super(id);
    this.fightId = fightId;
    this.firstBoxerId = firstBoxerId;
    this.secondBoxerId = secondBoxerId;
    this.accountId = accountId;
    this.firstBoxerScores = firstBoxerScores;
    this.secondBoxerScores = secondBoxerScores;
  }

  public static ScoreCard create(ScoreCardId id, AccountId accountId, FightId fightId, BoxerId firstBoxerId,
      BoxerId secondBoxerId) {
    return new ScoreCard(id, accountId, fightId, firstBoxerId, secondBoxerId, new HashMap<>(), new HashMap<>());
  }

  @Override
  public ScoreCardId id() {
    return id;
  }

  public FightId fightId() {
    return fightId;
  }

  public BoxerId firstBoxerId() {
    return firstBoxerId;
  }

  public BoxerId secondBoxerId() {
    return secondBoxerId;
  }

  public AccountId accountId() {
    return accountId;
  }

  public Integer firstBoxerScore() {
    return firstBoxerScores.values().stream().reduce(0, Integer::sum);
  }

  public Integer firstBoxerScore(int round) {
    return firstBoxerScores.get(round);
  }

  public Integer secondBoxerScore() {
    return secondBoxerScores.values().stream().reduce(0, Integer::sum);
  }

  public Integer secondBoxerScore(int round) {
    return secondBoxerScores.get(round);
  }

  public void scoreRound(int round, int firstBoxerScore, int secondBoxerScore, Instant scoredAt) {
    firstBoxerScores.put(round, firstBoxerScore);
    secondBoxerScores.put(round, secondBoxerScore);
    this.scoredAt = scoredAt;
    addDomainEvent(RoundScored.create(this, scoredAt));
  }

  public Instant scoredAt() {
    return scoredAt;
  }
}
