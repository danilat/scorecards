package com.danilat.scorecards.core.shared;

import java.util.UUID;

public class UniqueIdGenerator {

  public String next() {
    return UUID.randomUUID().toString();
  }
}
