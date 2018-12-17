package com.danilat.scorecards.repositories;

import java.util.UUID;

public class UniqueIdentifierGenerator {

  public String next() {
    return UUID.randomUUID().toString();
  }
}
