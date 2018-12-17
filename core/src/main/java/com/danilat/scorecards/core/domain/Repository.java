package com.danilat.scorecards.core.domain;

import java.util.Optional;

public interface Repository<ENTITY extends Entity, ID extends EntityId> {

  void save(ENTITY entity);

  ID nextId();

  Optional<ENTITY> get(ID id);
}
