package com.danilat.scorecards.shared.domain;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ErrorsTest {

  @Test
  public void givenAnExistingFieldNameThenHasError() {
    Errors errors = new Errors();
    Error error = new Error("a field", "a default message", "a message template");
    errors.add(error);

    assertTrue(errors.hasError(error.getFieldName()));
  }

  @Test
  public void givenAnNonExistingFieldNameThenDoNotHasError() {
    Errors errors = new Errors();

    assertFalse(errors.hasError("foobar"));
  }

}
