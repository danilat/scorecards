package com.danilat.scorecards.core.domain.score;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import java.util.HashMap;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
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
}
