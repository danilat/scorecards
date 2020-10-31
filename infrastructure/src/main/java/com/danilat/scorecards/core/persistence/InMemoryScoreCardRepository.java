package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.shared.persistence.InMemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryScoreCardRepository extends InMemoryRepository<ScoreCard, ScoreCardId> implements ScoreCardRepository {
}
