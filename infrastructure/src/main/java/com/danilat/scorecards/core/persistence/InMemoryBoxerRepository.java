package com.danilat.scorecards.core.persistence;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.shared.persistence.InMemoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBoxerRepository extends InMemoryRepository<Boxer, BoxerId> implements
    BoxerRepository {

}
