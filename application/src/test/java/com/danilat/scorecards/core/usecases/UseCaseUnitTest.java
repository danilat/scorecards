package com.danilat.scorecards.core.usecases;

import static org.mockito.Mockito.verify;

import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.Error;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public abstract class UseCaseUnitTest<SUCCESS_ENTITY> {

  @Captor
  public ArgumentCaptor<SUCCESS_ENTITY> successArgumentCaptor;

  @Captor
  ArgumentCaptor<Error> errorsArgumentCaptor;

  public abstract PrimaryPort getPrimaryPort();

  public FieldErrors getFieldErrors() {
    verify(getPrimaryPort()).error(errorsArgumentCaptor.capture());
    return (FieldErrors) errorsArgumentCaptor.getValue();
  }

  public Error getError() {
    verify(getPrimaryPort()).error(errorsArgumentCaptor.capture());
    return errorsArgumentCaptor.getValue();
  }

  public SUCCESS_ENTITY getSuccessEntity() {
    verify(getPrimaryPort()).success(successArgumentCaptor.capture());
    return successArgumentCaptor.getValue();
  }
}
