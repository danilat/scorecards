package com.danilat.scorecards.core.domain.boxer;

import java.util.Optional;

public interface BoxerRepository {
  Optional<Boxer> get(BoxerId id);
}
