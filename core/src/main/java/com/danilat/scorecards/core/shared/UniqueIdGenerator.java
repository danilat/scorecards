package com.danilat.scorecards.core.shared;

import java.util.UUID;

public class UUIDGenerator {

  public String next() {
    return UUID.randomUUID().toString();
  }
}
