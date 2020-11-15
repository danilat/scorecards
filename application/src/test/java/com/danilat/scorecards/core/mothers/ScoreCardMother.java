package com.danilat.scorecards.core.mothers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.shared.UniqueIdGenerator;

public class ScoreCardMother {

  public static ScoreCard aScoreCardWithId(ScoreCardId scoreCardId) {
    return aScoreCardWithIdFightIdFirstAndSecondBoxer(scoreCardId, new FightId("1"), new BoxerId("ALI"),
        new BoxerId("FOREMAN"));
  }

  public static ScoreCard aScoreCardWithIdAndAccount(String scoreCardId, String account) {
    return aScoreCardWithIdAndAccount(new ScoreCardId(scoreCardId), new AccountId(account));
  }

  public static ScoreCard aScoreCardWithIdAndAccount(ScoreCardId scoreCardId, AccountId account) {
    return ScoreCard.create(scoreCardId, account, new FightId("1"), new BoxerId("ALI"),
        new BoxerId("FOREMAN"));
  }

  public static ScoreCard aScoreCardWithIdFightIdFirstAndSecondBoxer(ScoreCardId scoreCardId, FightId fightId,
      BoxerId firstBoxer, BoxerId secondBoxer) {
    return ScoreCard.create(scoreCardId, new AccountId("foo"), fightId, firstBoxer, secondBoxer);
  }

  public static ScoreCardId nextId() {
    return new ScoreCardId(new UniqueIdGenerator().next());
  }
}
