package com.danilat.scorecards.shared.domain.errors;

public class SimpleError implements Error {

  private final String message;
  private final String messageTemplate;

  public SimpleError(String message, String messageTemplate) {
    this.message = message;
    this.messageTemplate = messageTemplate;
  }

  public SimpleError(String message) {
    this.message = message;
    this.messageTemplate = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }
}
