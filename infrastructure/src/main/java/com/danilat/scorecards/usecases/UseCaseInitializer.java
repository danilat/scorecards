package com.danilat.scorecards.usecases;

import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseInitializer {

  @Autowired
  private FightRepository fightRepository;

  @Autowired
  private BoxerRepository boxerRepository;

  @Bean
  public RetrieveAFight retrieveAFight() {
    return new RetrieveAFight(fightRepository);
  }
}

