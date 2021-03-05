package com.danilat.scorecards.shared.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FieldErrorsTest {

  @Test
  public void givenAnExistingFieldNameThenHasError() {
    FieldErrors errors = new FieldErrors();
    errors.add("a field", new Error("a default message", "a message template"));

    assertTrue(errors.hasError(FieldError.class));
  }

  @Test
  public void givenAnNonExistingFieldNameThenDoNotHasError() {
    FieldErrors errors = new FieldErrors();

    assertFalse(errors.hasError(FieldError.class));
  }

}
