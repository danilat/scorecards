package com.danilat.scorecards.core;

import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetailsFetcher;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount;
import com.danilat.scorecards.core.usecases.scores.RetrieveAScoreCard;
import com.danilat.scorecards.core.usecases.scores.RetrieveScoreCards;
import com.danilat.scorecards.shared.Auth;
import com.danilat.scorecards.core.usecases.boxers.RetrieveAllBoxers;
import com.danilat.scorecards.core.usecases.fights.RegisterFight;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.core.usecases.scores.ScoreRound;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.SharedInitializer;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.events.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import(SharedInitializer.class)
public class CoreInitializer {

  @Autowired
  private FightWithBoxersFetcher fightWithBoxersFetcher;

  @Autowired
  private FightRepository fightRepository;

  @Autowired
  private BoxerRepository boxerRepository;

  @Autowired
  private EventBus eventBus;

  @Autowired
  private Clock clock;

  @Autowired
  private UniqueIdGenerator uniqueIdGenerator;

  @Autowired
  private ScoreCardRepository scoreCardRepository;

  @Autowired
  private ScoreCardWithFightDetailsFetcher scoreCardWithFightDetailsFetcher;

  @Autowired
  private Auth auth;

  @Autowired
  private AccountRepository accountRepository;

  @Bean
  public RetrieveAFight retrieveAFight() {
    return new RetrieveAFight(fightWithBoxersFetcher);
  }

  @Bean
  public RegisterFight registerFight() {
    return new RegisterFight(fightRepository, boxerRepository, eventBus, clock, uniqueIdGenerator);
  }

  @Bean
  public RetrieveAllBoxers retrieveAllBoxers() {
    return new RetrieveAllBoxers(boxerRepository);
  }

  @Bean
  public ScoreRound scoreRound() {
    return new ScoreRound(scoreCardRepository, fightRepository, uniqueIdGenerator, auth, eventBus, clock);
  }

  @Bean
  public RetrieveAScoreCard retrieveAScoreCard() {
    return new RetrieveAScoreCard(scoreCardRepository);
  }

  @Bean
  public RetrieveScoreCards retrieveScoreCards() {
    return new RetrieveScoreCards(scoreCardWithFightDetailsFetcher);
  }

  @Bean
  public RegisterAccount registerAccount() {
    return new RegisterAccount(accountRepository, eventBus, clock);
  }
}

