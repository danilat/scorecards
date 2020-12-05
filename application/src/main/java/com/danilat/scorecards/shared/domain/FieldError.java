package com.danilat.scorecards.shared.domain;

public class FieldError {

  private final String fieldName;
  private final Error error;

  public FieldError(String fieldName, String message, String messageTemplate) {
    this.fieldName = fieldName;
    this.error = new Error(message, messageTemplate);
  }

  public FieldError(String fieldName, String message) {
    this.fieldName = fieldName;
    this.error = new Error(message, message);
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getMessage() {
    return error.getMessage();
  }

  public String getMessageTemplate() {
    return error.getMessageTemplate();
  }
}
