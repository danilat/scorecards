package com.danilat.scorecards.shared.domain;

import java.util.Optional;

public interface Repository<E extends Entity, I extends EntityId> {

  void save(E entity);

  Optional<E> get(I id);
}
