package com.danilat.scorecards.shared.usecases;

import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class WriteUseCase<OUTPUT extends Entity, INPUT> implements UseCase<OUTPUT, INPUT> {

  private final EventBus eventBus;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  protected WriteUseCase(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  public final void execute(PrimaryPort<OUTPUT> primaryPort, INPUT parameters) {
    FieldErrors errors = validate(parameters);
    if (errors.hasErrors()) {
      logger.debug("There are validation errors {}", errors);
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
