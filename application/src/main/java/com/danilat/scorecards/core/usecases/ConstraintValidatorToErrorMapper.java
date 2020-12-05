package com.danilat.scorecards.core.usecases;

import com.danilat.scorecards.shared.domain.FieldError;
import com.danilat.scorecards.shared.domain.Errors;
import java.util.Set;
import javax.validation.ConstraintViolation;

public class ConstraintValidatorToErrorMapper<T> {

  public FieldError mapConstraintViolationToError(ConstraintViolation violation) {
    String fieldName = violation.getPropertyPath().toString();
    String message = violation.getMessage();
    String messageTemplate = violation.getMessageTemplate();
    FieldError error = new FieldError(fieldName, message, messageTemplate);
    return error;
  }

  public Errors mapConstraintViolationsToErrors(Set<ConstraintViolation<T>> violations) {
    Errors errors = new Errors();
    if (!violations.isEmpty()) {
      violations.forEach(violation -> {
        FieldError error = mapConstraintViolationToError(violation);
        errors.add(error);
      });
    }
    return errors;
  }
}
