package com.danilat.scorecards.core.domain.score.events;

import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;

public class RoundScored extends DomainEvent {

  private ScoreCard scoreCard;

  public RoundScored(ScoreCard scoreCard, Instant happenedAt, DomainEventId eventId) {
    super(happenedAt, eventId);
    this.scoreCard = scoreCard;
  }

  public ScoreCard scoreCard() {
    return scoreCard;
  }

  public static RoundScored create(ScoreCard scoreCard, Instant happenedAt) {
    return new RoundScored(scoreCard, happenedAt, new DomainEventId(scoreCard.id().value() + happenedAt));
  }
}
