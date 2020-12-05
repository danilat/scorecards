package com.danilat.scorecards.shared.domain;

public class Error {
  private final String message;
  private final String messageTemplate;

  public Error(String message, String messageTemplate) {
    this.message = message;
    this.messageTemplate = messageTemplate;
  }

  public Error(String message) {
    this.message = message;
    this.messageTemplate = message;
  }

  public String getMessage() {
    return message;
  }

  public String getMessageTemplate() {
    return messageTemplate;
  }
}
