package com.danilat.scorecards.core.persistence.memory;

import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.shared.persistence.InMemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFightRepository extends InMemoryRepository<Fight, FightId> implements
    FightRepository {

}
