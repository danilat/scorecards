package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.shared.domain.Entity;

import java.util.HashMap;

public class ScoreCard extends Entity<ScoreCardId> {
    private final FightId fightId;
    private final BoxerId firstBoxerId;
    private final BoxerId secondBoxerId;
    private final HashMap<Integer, Integer> firstBoxerScores;
    private final HashMap<Integer, Integer> secondBoxerScores;
    private AccountId accountId;

    public ScoreCard(ScoreCardId id, AccountId accountId, FightId fightId, BoxerId firstBoxerId, BoxerId secondBoxerId) {
        super(id);
        this.fightId = fightId;
        this.firstBoxerId = firstBoxerId;
        this.secondBoxerId = secondBoxerId;
        this.accountId = accountId;
        this.firstBoxerScores = new HashMap<>();
        this.secondBoxerScores = new HashMap<>();
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

    public int firstBoxerScore() {
        return firstBoxerScores.values().stream().reduce(0, Integer::sum);
    }

    public int firstBoxerScore(int round) {
        return firstBoxerScores.get(round);
    }

    public int secondBoxerScore() {
        return secondBoxerScores.values().stream().reduce(0, Integer::sum);
    }

    public int secondBoxerScore(int round) {
        return secondBoxerScores.get(round);
    }

    public void scoreRound(int round, int firstBoxerScore, int secondBoxerScore) {
        firstBoxerScores.put(round, firstBoxerScore);
        secondBoxerScores.put(round, secondBoxerScore);
    }
}
