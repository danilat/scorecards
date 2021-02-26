package com.danilat.scorecards.core.persistence.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class RawToScores {

  public static Map<Integer, Integer> map(String raw) {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<Integer, Integer> scores;
    try {
      scores = objectMapper
          .readValue(raw, new TypeReference<Map<Integer, Integer>>() {
          });
      return scores;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
