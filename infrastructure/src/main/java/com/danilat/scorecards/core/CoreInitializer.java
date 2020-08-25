package com.danilat.scorecards.core;

import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersRepository;
import com.danilat.scorecards.core.usecases.fights.RegisterFight;
import com.danilat.scorecards.core.usecases.fights.RetrieveAFight;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.events.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class CoreInitializer {

  @Autowired
  private FightWithBoxersRepository fightWithBoxersRepository;

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


  @Bean
  public RetrieveAFight retrieveAFight() {
    return new RetrieveAFight(fightWithBoxersRepository);
  }

  @Bean
  public RegisterFight registerFight(){
    return new RegisterFight(fightRepository, boxerRepository, eventBus,clock,uniqueIdGenerator);
  }
}

