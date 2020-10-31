package com.danilat.scorecards.core.usecases.scores;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightNotFoundException;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.usecases.Auth;
import com.danilat.scorecards.shared.UniqueIdGenerator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;

public class ScoreRound {
    private ScoreCardRepository scoreCardRepository;
    private final FightRepository fightRepository;
    private final UniqueIdGenerator uniqueIdGenerator;
    private final Auth auth;

    public ScoreRound(ScoreCardRepository scoreCardRepository, FightRepository fightRepository, UniqueIdGenerator uniqueIdGenerator, Auth auth) {
        this.scoreCardRepository = scoreCardRepository;
        this.fightRepository = fightRepository;
        this.uniqueIdGenerator = uniqueIdGenerator;
        this.auth = auth;
    }

    public ScoreCard execute(ScoreFightParameters params) {
        validate(params);
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

    private void validate(ScoreFightParameters params) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<ScoreFightParameters>> violations = validator.validate(params);
        if (!violations.isEmpty()) {
            throw new InvalidScoreException(violations);
        }
        fightRepository.get(params.getFightId()).orElseThrow(() -> new FightNotFoundException(params.getFightId()));
    }

    public static class ScoreFightParameters {
        private final FightId fightId;
        @Min(value = 1, message = "round interval is between 1 and 12")
        @Max(value = 12, message = "round interval is between 1 and 12")
        private final int round;
        private final BoxerId firstBoxerId;
        private final int firstBoxerScore;
        private final BoxerId secondBoxerId;
        private final int secondBoxerScore;

        public ScoreFightParameters(FightId fightId, Integer round, BoxerId firstBoxerId, Integer firstBoxerScore, BoxerId secondBoxerId, Integer secondBoxerScore) {
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
