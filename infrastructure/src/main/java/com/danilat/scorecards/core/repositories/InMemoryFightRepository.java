package com.danilat.scorecards.core.repositories;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.shared.repositories.InMemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFightRepository extends InMemoryRepository<Fight, FightId> implements
    FightRepository {

}
