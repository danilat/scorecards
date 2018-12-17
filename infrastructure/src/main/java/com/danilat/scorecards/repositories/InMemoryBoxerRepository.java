package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;

public class InMemoryBoxerRepository extends InMemoryRepository<Boxer, BoxerId> implements BoxerRepository {

  @Override
  public BoxerId nextId() {
    String unique = new UniqueIdentifierGenerator().next();
    return new BoxerId(unique);
  }
}
