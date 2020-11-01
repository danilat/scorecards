package com.danilat.scorecards.shared.domain;

import javax.validation.ConstraintViolation;

public class Error {

  private final String fieldName;
  private final String message;
  private final String messageTemplate;

  public Error(ConstraintViolation violation) {
    fieldName = violation.getPropertyPath().toString();
    message = violation.getMessage();
    messageTemplate = violation.getMessageTemplate();
  }

  public Error(String fieldName, String message, String messageTemplate) {
    this.fieldName = fieldName;
    this.message = message;
    this.messageTemplate = messageTemplate;
  }

  public Error(String fieldName, String message) {
    this.fieldName = fieldName;
    this.message = message;
    this.messageTemplate = message;
  }

  public String getFieldName() {
    return fieldName;
  }

  public String getMessage() {
    return message;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }
}
