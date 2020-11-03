package com.danilat.scorecards.core.domain.score.events;

import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.LocalDate;

public class RoundScored extends DomainEvent {

  private ScoreCard scoreCard;

  public RoundScored(ScoreCard scoreCard, LocalDate happenedAt, DomainEventId eventId) {
    super(happenedAt, eventId);
    this.scoreCard = scoreCard;
  }

  public ScoreCard scoreCard() {
    return scoreCard;
  }
}
