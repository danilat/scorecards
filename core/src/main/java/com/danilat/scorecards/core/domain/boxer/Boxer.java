package com.danilat.scorecards.core.domain.boxer;

public class Boxer {
  private String id;
  private String name;

  public Boxer(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }
}
