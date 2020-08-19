package com.danilat.scorecards;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScorecardsApplication {

  private static final Logger log = LoggerFactory.getLogger(ScorecardsApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(ScorecardsApplication.class, args);
  }

  @Autowired
  FightRepository fightRepository;

  @Autowired
  BoxerRepository boxerRepository;

  @PostConstruct
  public void init() {
    log.info("ScorecardsApplication initialization logic ...");
    Boxer ali = new Boxer(new BoxerId("ali"), "Muhammad Ali");
    boxerRepository.save(ali);
    Boxer foreman = new Boxer(new BoxerId("foreman"), "George Foreman");
    boxerRepository.save(foreman);
    Event event = new Event(LocalDate.now(), "Kinasa, Zaire");
    Fight fight = new Fight(new FightId("1"), ali.id(), foreman.id(), event, 12);
    fightRepository.save(fight);
  }
}
