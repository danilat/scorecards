package com.danilat.scorecards.shared.domain;

import com.danilat.scorecards.core.domain.score.ScoreCardNotFoundError;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Errors extends ArrayList<Error> {

  public static Errors newWithError(Error error){
    Errors errors = new Errors();
    errors.add(error);
    return errors;
  }

  public boolean hasError(String fieldName) {
    return this.stream().anyMatch(error -> error.getFieldName().equals(fieldName));
  }

  public boolean hasMessage(String message) {
    return this.stream().anyMatch(error -> error.getMessage().equals(message));
  }

  public boolean hasError(Class<ScoreCardNotFoundError> scoreCardNotFoundErrorClass) {
    return this.stream().anyMatch(error -> error.getClass().equals(scoreCardNotFoundErrorClass));
  }

  public Stream<String> getMessagesFor(String fieldName) {
    return this.stream().filter(error -> error.getFieldName().equals(fieldName)).map(error -> error.getMessage());
  }

  public String getMessagesContentFor(String fieldName) {
    return this.getMessagesFor(fieldName).collect(Collectors.joining(". "));
  }

  @Override
  public boolean add(Error error) {
    if(error != null){
      return super.add(error);
    }else {
      return false;
    }
  }
}
