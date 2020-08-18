package com.danilat.scorecards.core.domain;

import java.util.Optional;

public interface Repository<E extends Entity, I extends EntityId> {

  void save(E entity);

  I nextId();

  Optional<E> get(I id);
}
