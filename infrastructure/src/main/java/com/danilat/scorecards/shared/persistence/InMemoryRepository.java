package com.danilat.scorecards.shared.persistence;

import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.domain.Id;
import com.danilat.scorecards.shared.domain.Repository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<E extends Entity, I extends Id> implements
    Repository<E, I> {

  protected final Map<Id, E> entities;

  public InMemoryRepository() {
    this.entities = new HashMap<>();
  }

  public void save(E entity) {
    entities.put(entity.id(), entity);
  }

  public Optional<E> get(I id) {
    return Optional.ofNullable(entities.get(id));
  }

  public Collection<E> all() {
    return entities.values();
  }
}
