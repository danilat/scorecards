package com.danilat.scorecards.shared.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldErrors implements Iterable<FieldError>, Errors {

  private final List<FieldError> values;

  public static FieldErrors newWithError(String fieldName, Error error) {
    FieldErrors errors = new FieldErrors();
    errors.add(fieldName, error);
    return errors;
  }

  public FieldErrors() {
    values = new ArrayList<>();
  }

  public boolean hasMessage(String message) {
    return values.stream().anyMatch(error -> error.getMessage().equals(message));
  }

  public boolean hasError(String fieldName) {
    return values.stream().anyMatch(error -> error.getFieldName().equals(fieldName));
  }

  public boolean hasError(Class errorClass) {
    return values.stream().anyMatch(error -> error.getClass().equals(errorClass));
  }

  public Stream<String> getMessagesFor(String fieldName) {
    return values.stream().filter(error -> error.getFieldName().equals(fieldName)).map(error -> error.getMessage());
  }

  public String getMessagesContentFor(String fieldName) {
    return this.getMessagesFor(fieldName).collect(Collectors.joining(". "));
  }

  public boolean add(String fieldName, Error error) {
    if (error != null) {
      return values.add(new FieldError(fieldName, error));
    } else {
      return false;
    }
  }

  public void addAll(FieldErrors fieldErrors) {
    values.addAll(fieldErrors.values);
  }

  public boolean hasErrors() {
    return !values.isEmpty();
  }

  @Override
  public Iterator<FieldError> iterator() {
    return values.iterator();
  }

  @Override
  public void forEach(Consumer<? super FieldError> action) {
    values.forEach(action);
  }

  @Override
  public Spliterator<FieldError> spliterator() {
    return values.spliterator();
  }
}
