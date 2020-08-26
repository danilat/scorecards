package com.danilat.scorecards.shared.domain;

import java.util.Map;
import java.util.Optional;

public interface Repository<E extends Entity, I extends Id> {

  void save(E entity);

  Optional<E> get(I id);

  Map<I, E> all();
}