package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBoxerRepository extends InMemoryRepository<Boxer, BoxerId> implements
    BoxerRepository {

}
