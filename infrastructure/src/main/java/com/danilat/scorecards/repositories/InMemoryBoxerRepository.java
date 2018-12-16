package com.danilat.scorecards.repositories;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryBoxerRepository implements BoxerRepository {

  private final Map<BoxerId, Boxer> boxers;

  public InMemoryBoxerRepository() {
    this.boxers = new HashMap<>();
  }

  @Override
  public Optional<Boxer> get(BoxerId id) {
    return Optional.ofNullable(boxers.get(id));
  }

  @Override
  public void save(Boxer boxer) {
    boxers.put(boxer.id(), boxer);
  }
}
