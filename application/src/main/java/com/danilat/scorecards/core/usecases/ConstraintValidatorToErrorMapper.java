package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.FieldErrors;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ConstraintValidatorToErrorMapper<T> {

  public Error mapConstraintViolationToError(ConstraintViolation violation) {
    String message = violation.getMessage();
    String messageTemplate = violation.getMessageTemplate();
    return new Error(message, messageTemplate);
  }

  public FieldErrors mapConstraintViolationsToErrors(Set<ConstraintViolation> violations) {
    FieldErrors errors = new FieldErrors();
    if (!violations.isEmpty()) {
      violations.forEach(violation -> {
        String fieldName = violation.getPropertyPath().toString();
        Error error = mapConstraintViolationToError(violation);
        errors.add(fieldName, error);
      });
    }
    return errors;
  }
}
