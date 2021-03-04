package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.FieldError;
import com.danilat.scorecards.shared.domain.FieldErrors;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ConstraintValidatorToErrorMapper<T> {

  public FieldError mapConstraintViolationToError(ConstraintViolation violation) {
    String fieldName = violation.getPropertyPath().toString();
    String message = violation.getMessage();
    String messageTemplate = violation.getMessageTemplate();
    FieldError error = new FieldError(fieldName, new Error(message, messageTemplate));
    return error;
  }

  public FieldErrors mapConstraintViolationsToErrors(Set<ConstraintViolation> violations) {
    FieldErrors errors = new FieldErrors();
    if (!violations.isEmpty()) {
      violations.forEach(violation -> {
        FieldError error = mapConstraintViolationToError(violation);
        errors.add(error);
      });
    }
    return errors;
  }
}
