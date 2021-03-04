package com.danilat.scorecards.core.domain.score;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoxerScores {

  private final Map<Integer, Integer> map;

  public BoxerScores(Map<Integer, Integer> map) {
    this.map = map;
  }

  public BoxerScores() {
    map = new HashMap<>();
  }

  public Map<Integer, Integer> values() {
    return map;
  }

  public Integer total() {
    return map.values().stream().reduce(0, Integer::sum);
  }

  public Integer inRound(int round) {
    return map.get(round);
  }

  public void add(int round, int score) {
    map.put(round, score);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoxerScores that = (BoxerScores) o;
    return Objects.equals(map, that.map);
  }

  @Override
  public int hashCode() {
    return Objects.hash(map);
  }
}
