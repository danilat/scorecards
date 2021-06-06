package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundError;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.WriteUseCase;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class UpdateFight extends WriteUseCase<Fight, UpdateFight.UpdateFightParams> {
  private final FightRepository fightRepository;
  private final BoxerRepository boxerRepository;
  private final Clock clock;

  public UpdateFight(FightRepository fightRepository, BoxerRepository boxerRepository, EventBus eventBus, Clock clock) {
    super(eventBus);
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
    this.clock = clock;
  }

  @Override
  protected Fight executeWhenValid(UpdateFightParams params) {
    Fight fight = fightRepository.get(params.getFightId()).get();
    fight.update(params.getFirstBoxerId(), params.getSecondBoxerId(), params.getNumberOfRounds(), params.getEvent(), clock.now());
    fightRepository.save(fight);
    return fight;
  }

  protected FieldErrors validate(UpdateFightParams params) {
    FieldErrors errors = new FieldErrors();
    errors.addAll(params.validate());
    errors.add("firstBoxer", validateBoxer(params.getFirstBoxerId()));
    errors.add("secondBoxer", validateBoxer(params.getSecondBoxerId()));
    return errors;
  }

  private BoxerNotFoundError validateBoxer(BoxerId boxerId) {
    BoxerNotFoundError error = null;
    if (!this.boxerRepository.get(boxerId).isPresent()) {
      error = new BoxerNotFoundError(boxerId);
    }
    return error;
  }

  public static class UpdateFightParams extends ValidatableParameters {
    private final FightId fightId;
    private final BoxerId firstBoxerId;
    private final BoxerId secondBoxerId;
    @NotNull(message = "happenAt is mandatory")
    private final LocalDate happenAt;
    private final String place;
    @NotNull(message = "numberOfRounds is mandatory")
    @Min(value = 3, message = "numberOfRounds is less than three")
    @Max(value = 12, message = "numberOfRounds is more than twelve")
    private final Integer numberOfRounds;

    public FightId getFightId() {
      return fightId;
    }

    public BoxerId getFirstBoxerId() {
      return firstBoxerId;
    }

    public BoxerId getSecondBoxerId() {
      return secondBoxerId;
    }

    public LocalDate getHappenAt() {
      return happenAt;
    }

    public String getPlace() {
      return place;
    }

    public Integer getNumberOfRounds() {
      return numberOfRounds;
    }

    public UpdateFightParams(FightId fightId, BoxerId firstBoxerId, BoxerId secondBoxerId, LocalDate happenAt, String place, Integer numberOfRounds) {
      super();
      this.fightId = fightId;
      this.firstBoxerId = firstBoxerId;
      this.secondBoxerId = secondBoxerId;
      this.happenAt = happenAt;
      this.place = place;
      this.numberOfRounds = numberOfRounds;
    }

    public Event getEvent() {
      return new Event(happenAt, place);
    }
  }
}
