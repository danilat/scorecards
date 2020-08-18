package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.Entity;
import com.danilat.scorecards.core.domain.EntityId;
import com.danilat.scorecards.core.domain.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<E extends Entity, I extends EntityId> implements Repository<E, I> {

  protected final Map<EntityId, E> entities;

  public InMemoryRepository() {
    this.entities = new HashMap<>();
  }

  public void save(E entity) {
    entities.put(entity.id(), entity);
  }

  public Optional<E> get(I id) {
    return Optional.ofNullable(entities.get(id));
  }
}
