package com.danilat.scorecards.shared;

import com.danilat.scorecards.shared.domain.Errors;

public interface PrimaryPort<E> {

  void success(E entity);

  void error(Errors errors);
}
