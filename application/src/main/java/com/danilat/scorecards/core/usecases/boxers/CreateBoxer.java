package com.danilat.scorecards.core.usecases.boxers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.usecases.boxers.CreateBoxer.CreateBoxerParams;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.WriteUseCase;
import javax.validation.constraints.NotEmpty;

public class CreateBoxer extends WriteUseCase<Boxer, CreateBoxerParams> {

  private final UniqueIdGenerator uniqueIdGenerator;
  private final BoxerRepository boxerRepository;
  private final Clock clock;

  public CreateBoxer(UniqueIdGenerator uniqueIdGenerator,
      BoxerRepository boxerRepository, EventBus eventBus, Clock clock) {
    super(eventBus);
    this.uniqueIdGenerator = uniqueIdGenerator;
    this.boxerRepository = boxerRepository;
    this.clock = clock;
  }

  @Override
  public Boxer executeWhenValid(CreateBoxerParams params) {
    BoxerId boxerId = new BoxerId(uniqueIdGenerator.next());
    Boxer boxer = Boxer.create(boxerId, params.getName(), params.getAlias(), params.getBoxrecUrl(), clock.now());
    boxerRepository.save(boxer);
    return boxer;
  }

  protected FieldErrors validate(CreateBoxerParams params) {
    return params.validate();
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
