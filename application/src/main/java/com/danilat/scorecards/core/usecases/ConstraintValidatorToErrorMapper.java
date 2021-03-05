package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.shared.domain.errors.SimpleError;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ConstraintValidatorToErrorMapper<T> {

  public SimpleError mapConstraintViolationToError(ConstraintViolation violation) {
    String message = violation.getMessage();
    String messageTemplate = violation.getMessageTemplate();
    return new SimpleError(message, messageTemplate);
  }

  public FieldErrors mapConstraintViolationsToErrors(Set<ConstraintViolation> violations) {
    FieldErrors errors = new FieldErrors();
    if (!violations.isEmpty()) {
      violations.forEach(violation -> {
        String fieldName = violation.getPropertyPath().toString();
        SimpleError error = mapConstraintViolationToError(violation);
        errors.add(fieldName, error);
      });
    }
    return errors;
  }
}
