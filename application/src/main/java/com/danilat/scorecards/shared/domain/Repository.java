package com.danilat.scorecards.shared.domain;

import java.util.List;
import java.util.Optional;

public interface Repository<E extends Entity, I extends Id> {

  void save(E entity);

  Optional<E> get(I id);

  List<E> all();
}
