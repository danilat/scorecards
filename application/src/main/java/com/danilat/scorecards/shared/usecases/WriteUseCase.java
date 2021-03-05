package com.danilat.scorecards.shared.usecases;

import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;

public abstract class WriteUseCase<OUTPUT extends Entity, INPUT> implements UseCase<OUTPUT, INPUT> {
  private final EventBus eventBus;

  protected WriteUseCase(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public void execute(PrimaryPort<OUTPUT> primaryPort, INPUT parameters) {
    FieldErrors errors = validate(parameters);
    if (!errors.isEmpty()) {
      primaryPort.error(errors);
      return;
    }
    OUTPUT output = executeWhenValid(parameters);
    eventBus.publish(output.domainEvents());
    primaryPort.success(output);
  }

  protected abstract OUTPUT executeWhenValid(INPUT parameters);

  protected abstract FieldErrors validate(INPUT parameters);
}
