package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.Entity;
import com.danilat.scorecards.core.domain.EntityId;
import com.danilat.scorecards.core.domain.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class InMemoryRepository<ENTITY extends Entity, ID extends EntityId> implements Repository<ENTITY, ID> {

  protected final Map<EntityId, ENTITY> entities;

  public InMemoryRepository() {
    this.entities = new HashMap<>();
  }

  public void save(ENTITY entity) {
    entities.put(entity.id(), entity);
  }

  public Optional<ENTITY> get(ID id) {
    return Optional.ofNullable(entities.get(id));
  }
}
