package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundError;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.WriteUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RegisterFight extends WriteUseCase<Fight, RegisterFightParameters> {

  private final FightRepository fightRepository;
  private final BoxerRepository boxerRepository;
  private final Clock clock;
  private final UniqueIdGenerator uniqueIdGenerator;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public RegisterFight(FightRepository fightRepository, BoxerRepository boxerRepository,
      EventBus eventBus, Clock clock,
      UniqueIdGenerator uniqueIdGenerator) {
    super(eventBus);
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
    this.clock = clock;
    this.uniqueIdGenerator = uniqueIdGenerator;
  }

  public Fight executeWhenValid(RegisterFightParameters parameters) {
    Event event = new Event(parameters.getHappenAt(), parameters.getPlace());
    Fight fight = Fight.create(new FightId(uniqueIdGenerator.next()), parameters.getFirstBoxer(),
        parameters.getSecondBoxer(),
        event, parameters.getNumberOfRounds(), clock.now());
    fightRepository.save(fight);
    logger.debug("Fight saved {}", fight);
    return fight;
  }

  protected FieldErrors validate(RegisterFightParameters parameters) {
    FieldErrors errors = new FieldErrors();
    errors.addAll(parameters.validate());
    errors.add("firstBoxer", validateBoxer(parameters.getFirstBoxer()));
    errors.add("secondBoxer", validateBoxer(parameters.getSecondBoxer()));
    return errors;
  }

  private BoxerNotFoundError validateBoxer(BoxerId boxerId) {
    BoxerNotFoundError error = null;
    if (!this.boxerRepository.get(boxerId).isPresent()) {
      error = new BoxerNotFoundError(boxerId);
    }
    return error;
  }

  public static class RegisterFightParameters extends ValidatableParameters {

    @NotNull(message = "firstBoxer is mandatory")
    private final BoxerId firstBoxer;
    @NotNull(message = "secondBoxer is mandatory")
    private final BoxerId secondBoxer;
    @NotNull(message = "happenAt is mandatory")
    private final LocalDate happenAt;
    private final String place;
    @NotNull(message = "numberOfRounds is mandatory")
    @Min(value = 3, message = "numberOfRounds is less than three")
    @Max(value = 12, message = "numberOfRounds is more than twelve")
    private final Integer numberOfRounds;

    @AssertFalse(message = "firstBoxer and secondBoxer should be different")
    public boolean isFirstBoxersEqualThanSecondBoxer() {
      if (getFirstBoxer() == null) {
        return true;
      }
      return getFirstBoxer().equals(getSecondBoxer());
    }


    public BoxerId getFirstBoxer() {
      return firstBoxer;
    }

    public BoxerId getSecondBoxer() {
      return secondBoxer;
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


    public RegisterFightParameters(BoxerId firstBoxer, BoxerId secondBoxer, LocalDate happenAt,
        String place, Integer numberOfRounds) {
      this.firstBoxer = firstBoxer;
      this.secondBoxer = secondBoxer;
      this.happenAt = happenAt;
      this.place = place;
      this.numberOfRounds = numberOfRounds;
    }
  }
}
