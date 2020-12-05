package com.danilat.scorecards.shared.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ErrorsTest {

  @Test
  public void givenAnExistingFieldNameThenHasError() {
    Errors errors = new Errors();
    FieldError error = new FieldError("a field", "a default message", "a message template");
    errors.add(error);

    assertTrue(errors.hasError(FieldError.class));
  }

  @Test
  public void givenAnNonExistingFieldNameThenDoNotHasError() {
    Errors errors = new Errors();

    assertFalse(errors.hasError(FieldError.class));
  }

}
