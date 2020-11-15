package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import java.time.Instant;

public class ScoreCardMother {

  public static ScoreCard aScoreCardWithId(ScoreCardId scoreCardId) {
    return aScoreCardWithIdFightIdFirstAndSecondBoxer(scoreCardId, new FightId("1"), new BoxerId("ALI"),
        new BoxerId("FOREMAN"));
  }

  public static ScoreCard aScoreCardWithIdAndAccount(String scoreCardId, String account) {
    return aScoreCardWithIdAndAccount(new ScoreCardId(scoreCardId), new AccountId(account));
  }

  public static ScoreCard aScoreCardWithIdAndAccount(ScoreCardId scoreCardId, AccountId account) {
    ScoreCard scoreCard = ScoreCard.create(scoreCardId, account, new FightId("1"), new BoxerId("ALI"),
        new BoxerId("FOREMAN"));
    scoreCard.scoreRound(1, 10, 9, Instant.now());
    return scoreCard;
  }

  public static ScoreCard aScoreCardWithIdFightIdFirstAndSecondBoxer(ScoreCardId scoreCardId, FightId fightId,
      BoxerId firstBoxer, BoxerId secondBoxer) {
    ScoreCard scoreCard =  ScoreCard.create(scoreCardId, new AccountId("foo"), fightId, firstBoxer, secondBoxer);
    scoreCard.scoreRound(1, 10, 9, Instant.now());
    return scoreCard;
  }

  public static ScoreCardId nextId() {
    return new ScoreCardId(new UniqueIdGenerator().next());
  }
}
