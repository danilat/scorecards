package com.danilat.scorecards.core.usecases.boxers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer.CreateBoxerParams;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.UseCase;
import javax.validation.constraints.NotEmpty;

public class CreateBoxer implements UseCase<Boxer, CreateBoxerParams> {

  private final UniqueIdGenerator uniqueIdGenerator;
  private final BoxerRepository boxerRepository;
  private final EventBus eventBus;
  private final Clock clock;
  private FieldErrors errors;

  public CreateBoxer(UniqueIdGenerator uniqueIdGenerator,
      BoxerRepository boxerRepository, EventBus eventBus, Clock clock) {
    this.uniqueIdGenerator = uniqueIdGenerator;
    this.boxerRepository = boxerRepository;
    this.eventBus = eventBus;
    this.clock = clock;
  }

  @Override
  public void execute(PrimaryPort<Boxer> primaryPort, CreateBoxerParams params) {
    if (!validate(params)) {
      primaryPort.error(errors);
      return;
    }

    BoxerId boxerId = new BoxerId(uniqueIdGenerator.next());
    Boxer boxer = Boxer.create(boxerId, params.getName(), params.getAlias(), params.getBoxrecUrl(), clock.now());
    boxerRepository.save(boxer);
    eventBus.publish(boxer.domainEvents());
    primaryPort.success(boxer);
  }

  private boolean validate(CreateBoxerParams params) {
    errors = new FieldErrors();
    errors.addAll(params.validate());
    return errors.isEmpty();
  }

  public static class CreateBoxerParams extends ValidatableParameters {

    @NotEmpty(message = "name is mandatory")
    private final String name;
    private final String alias;
    private final String boxrecUrl;

    public CreateBoxerParams(String name, String alias, String boxrecUrl) {
      this.name = name;
      this.alias = alias;
      this.boxrecUrl = boxrecUrl;
    }

    public String getName() {
      return name;
    }

    public String getAlias() {
      return alias;
    }

    public String getBoxrecUrl() {
      return boxrecUrl;
    }

    public static CreateBoxerParams empty() {
      return new CreateBoxerParams("", "", "");
    }
  }
}
