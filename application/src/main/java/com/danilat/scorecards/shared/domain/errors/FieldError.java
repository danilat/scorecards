package com.danilat.scorecards.shared.domain.errors;

public class FieldError {

  private final String fieldName;
  private final SimpleError error;

  public FieldError(String fieldName, SimpleError error) {
    this.fieldName = fieldName;
    this.error = error;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getMessage() {
    return error.getMessage();
  }
}
