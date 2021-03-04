package com.danilat.scorecards.shared.usecases;

import com.danilat.scorecards.core.usecases.ConstraintValidatorToErrorMapper;
import com.danilat.scorecards.shared.domain.FieldErrors;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public abstract class ValidatableParameters {

  public FieldErrors validate() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    ConstraintValidatorToErrorMapper constraintValidatorToErrorMapper = new ConstraintValidatorToErrorMapper();
    return constraintValidatorToErrorMapper.mapConstraintViolationsToErrors(validator.validate(this));
  }
}
