package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;

public class ScoreCardMother {
    public static ScoreCard aScoreCardWithId(ScoreCardId scoreCardId) {
        return new ScoreCard(scoreCardId, new AccountId("foo"), new FightId("1"), new BoxerId("ALI"), new BoxerId("FOREMAN"));
    }
}
