package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ConstraintValidatorToErrorMapper<T> {

  public Error mapConstraintViolationToError(ConstraintViolation violation) {
    String fieldName = violation.getPropertyPath().toString();
    String message = violation.getMessage();
    String messageTemplate = violation.getMessageTemplate();
    Error error = new Error(fieldName, message, messageTemplate);
    return error;
  }

  public Errors mapConstraintViolationsToErrors(Set<ConstraintViolation<T>> violations) {
    Errors errors = new Errors();
    if (!violations.isEmpty()) {
      violations.forEach(violation -> {
        Error error = mapConstraintViolationToError(violation);
        errors.add(error);
      });
    }
    return errors;
  }
}
