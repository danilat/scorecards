package com.danilat.scorecards.core.usecases.boxers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.usecases.boxers.UpdateBoxer.UpdateBoxerParams;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.WriteUseCase;

public class UpdateBoxer extends WriteUseCase<Boxer, UpdateBoxerParams> {

  private final BoxerRepository boxerRepository;
  private final Clock clock;

  public UpdateBoxer(BoxerRepository boxerRepository, EventBus eventBus,
      Clock clock) {
    super(eventBus);
    this.boxerRepository = boxerRepository;
    this.clock = clock;
  }

  @Override
  protected Boxer executeWhenValid(UpdateBoxerParams params) {
    Boxer boxer = boxerRepository.get(params.getBoxerId()).get();
    boxer.change(params.getName(), params.getAlias(), params.getBoxrecUrl(), clock.now());
    boxerRepository.save(boxer);
    return boxer;
  }

  @Override
  protected FieldErrors validate(UpdateBoxerParams parameters) {
    return new FieldErrors();
  }

  public static class UpdateBoxerParams {

    private final BoxerId boxerId;
    private final String name;
    private final String alias;
    private final String boxrecUrl;

    public UpdateBoxerParams(BoxerId boxerId, String name, String alias,
        String boxrecUrl) {
      this.boxerId = boxerId;
      this.name = name;
      this.alias = alias;
      this.boxrecUrl = boxrecUrl;
    }

    public BoxerId getBoxerId() {
      return boxerId;
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
  }
}
