package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.usecases.UseCase;
import java.util.Optional;

public class RetrieveAScoreCard implements UseCase<ScoreCard, ScoreCardId> {

  private final ScoreCardRepository scoreCardRepository;

  public RetrieveAScoreCard(ScoreCardRepository scoreCardRepository) {
    this.scoreCardRepository = scoreCardRepository;
  }

  public void execute(PrimaryPort<ScoreCard> primaryPort, ScoreCardId id) {
    Optional<ScoreCard> optionalScoreCard = this.scoreCardRepository.get(id);
    if(optionalScoreCard.isPresent()){
      primaryPort.success(optionalScoreCard.get());
    } else {
      Error error = new Error("scoreCardId", id + " not found");
      Errors errors = new Errors();
      errors.add(error);
      primaryPort.error(errors);
    }
  }
}
