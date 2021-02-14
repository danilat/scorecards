package com.danilat.scorecards.acceptation.steps;

import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class CleanRepositories {

  @Autowired
  private ScoreCardRepository scoreCardRepository;
  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private FightRepository fightRepository;
  @Autowired
  private BoxerRepository boxerRepository;

  @Before
  public void cleanRepositories() {
    scoreCardRepository.clear();
    fightRepository.clear();
    boxerRepository.clear();
    accountRepository.clear();
  }
}
