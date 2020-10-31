package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.usecases.Auth;
import com.danilat.scorecards.shared.UniqueIdGenerator;

public class ScoreRound {
    private ScoreCardRepository scoreCardRepository;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final Auth auth;

    public ScoreRound(ScoreCardRepository scoreCardRepository, UniqueIdGenerator uniqueIdGenerator, Auth auth) {
        this.scoreCardRepository = scoreCardRepository;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.auth = auth;
    }

    public ScoreCard execute(ScoreFightParameters params) {
        AccountId accountId = auth.currentAccount();
        ScoreCard scoreCard = this.scoreCardRepository.findByFightIdAndAccountId(params.getFightId(), accountId)
                .orElseGet(() -> {
                    ScoreCardId id = new ScoreCardId(uniqueIdGenerator.next());
                    return ScoreCard.create(id, accountId, params.getFightId(), params.getFirstBoxerId(), params.getSecondBoxerId());
                });
        scoreCard.scoreRound(params.getRound(), params.getFirstBoxerScore(), params.getSecondBoxerScore());
        this.scoreCardRepository.save(scoreCard);
        return scoreCard;
    }

    public static class ScoreFightParameters {
        private final FightId fightId;
        private final int round;
        private final BoxerId firstBoxerId;
        private final int firstBoxerScore;
        private final BoxerId secondBoxerId;
        private final int secondBoxerScore;

        public ScoreFightParameters(FightId fightId, int round, BoxerId firstBoxerId, int firstBoxerScore, BoxerId secondBoxerId, int secondBoxerScore) {
            this.fightId = fightId;
            this.round = round;
            this.firstBoxerId = firstBoxerId;
            this.firstBoxerScore = firstBoxerScore;
            this.secondBoxerId = secondBoxerId;
            this.secondBoxerScore = secondBoxerScore;
        }

        public FightId getFightId() {
            return fightId;
        }

        public int getRound() {
            return round;
        }

        public BoxerId getFirstBoxerId() {
            return firstBoxerId;
        }

        public int getFirstBoxerScore() {
            return firstBoxerScore;
        }

        public BoxerId getSecondBoxerId() {
            return secondBoxerId;
        }

        public int getSecondBoxerScore() {
            return secondBoxerScore;
        }
    }
}
