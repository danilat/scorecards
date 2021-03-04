package com.danilat.scorecards.core.persistence.mappers;

import com.danilat.scorecards.core.domain.score.BoxerScores;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class RawToScores {

  public static BoxerScores map(String raw) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      Map<Integer, Integer> scoresValues = objectMapper
          .readValue(raw, new TypeReference<Map<Integer, Integer>>() {
          });
      return new BoxerScores(scoresValues);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
