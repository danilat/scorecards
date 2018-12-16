package com.danilat.scorecards.core.domain.boxer;

import com.danilat.scorecards.core.domain.EntityId;

public class BoxerId implements EntityId {

  private final String id;

  public BoxerId(String id) {

    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }
}
