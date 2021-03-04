package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BoxerScores {

  private final Map<Integer, Integer> map;
  private final BoxerId boxerId;

  public BoxerScores(BoxerId boxerId, Map<Integer, Integer> map) {
    this.map = map;
    this.boxerId = boxerId;
  }

  public BoxerScores(BoxerId boxerId) {
    this.boxerId = boxerId;
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

  public BoxerId boxerId() {
    return boxerId;
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
    return Objects.equals(map, that.map) &&
        Objects.equals(boxerId, that.boxerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(map, boxerId);
  }

  @Override
  public String toString() {
    return "BoxerScores{" +
        "map=" + map +
        ", boxerId=" + boxerId +
        '}';
  }
}
