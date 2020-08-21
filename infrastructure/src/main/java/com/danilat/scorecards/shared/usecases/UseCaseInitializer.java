package com.danilat.scorecards.shared.usecases;

import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseInitializer {

  @Autowired
  private FightWithBoxersRepository fightWithBoxersRepository;

  @Bean
  public RetrieveAFight retrieveAFight() {
    return new RetrieveAFight(fightWithBoxersRepository);
  }
}

