package com.danilat.scorecards.shared;

import java.util.UUID;

public class UniqueIdGenerator {

  public String next() {
    return UUID.randomUUID().toString();
  }
}
